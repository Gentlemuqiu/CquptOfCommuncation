package com.example.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyun.oss.model.StorageClass;
import com.example.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${server.port}")
    private String port;

    @Value("${file.ip}")
    private String ip;

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * 上传文件接口
     *
     * @param file 上传的文件
     * @return 上传结果和文件访问路径
     * @throws IOException 文件写入失败时抛出
     */
    @PostMapping("/upload")
    public Result<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return Result.error("-1","上传文件不能为空！");
        }
        String accessKeyId = "xxx";
        String accessKeySecret = "xxx";

        String region = "cn-chengdu";
        String endpoint = "https://oss-cn-chengdu.aliyuncs.com";

         // 创建凭证提供者
        DefaultCredentialProvider provider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);

        // 配置客户端参数
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        // 显式声明使用V4签名算法
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);

        // 初始化OSS客户端
        OSS ossClient = OSSClientBuilder.create()
                .credentialsProvider(provider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .endpoint(endpoint)
                .build();

        // 2. 生成唯一文件名（避免重复）
        String fileName = UUID.randomUUID().toString();
        if (StrUtil.isNotEmpty(file.getOriginalFilename()) && file.getOriginalFilename().contains(".")) {
            String extension = StrUtil.subAfter(file.getOriginalFilename(), ".", true);
            fileName += "." + extension;
        }

        // 3. 拼接OSS中的文件完整路径
        String objectName = fileName;

        // 4. 获取文件输入流
        byte[] fileBytes = file.getBytes();
        try (InputStream inputStream = new java.io.ByteArrayInputStream(fileBytes)) {
            PutObjectRequest putObjectRequest = new PutObjectRequest("jxlxh", objectName, inputStream);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            metadata.setObjectAcl(CannedAccessControlList.PublicRead);
            putObjectRequest.setMetadata(metadata);
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            if (result.getResponse() != null) {
                throw new RuntimeException("文件上传失败：" + result.getResponse().toString());
            }
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败：" + e.getMessage(), e);
        }


        return Result.success("https://jxlxh.oss-cn-chengdu.aliyuncs.com/" + objectName);
    }

    /**
     * 父文本文件上传接口
     *
     * @param file 上传的文件
     * @return 编辑器需要的 JSON 格式结果
     * @throws IOException 文件写入失败时抛出
     */
    @PostMapping("/editor/upload")
    public JSONObject editorUpload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            JSONObject error = new JSONObject();
            error.set("errno", 1);
            error.set("message", "上传文件不能为空！");
            return error;
        }

        // 文件名和路径安全处理
        String originalFilename = file.getOriginalFilename();
        String flag = IdUtil.fastSimpleUUID(); // 生成唯一标识
        String safeFilename = originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_"); // 过滤非法字符
        String filePath = Paths.get(uploadDir, flag + "_" + safeFilename).toString();

        // 写入文件
        FileUtil.writeBytes(file.getBytes(), filePath);

        // 返回符合编辑器要求的结果
        String fileUrl = "http://" + ip + ":" + port + "/files/" + flag + "_" + safeFilename;
        JSONObject json = new JSONObject();
        json.set("errno", 0);
        JSONArray dataArray = new JSONArray();
        JSONObject data = new JSONObject();
        data.set("url", fileUrl);
        dataArray.add(data);
        json.set("data", dataArray);
        return json;
    }

    /**
     * 文件下载接口
     *
     * @param filename 文件名（带唯一标识）
     * @param response HTTP 响应
     */
    @GetMapping("/{filename}")
    public void downloadFile(@PathVariable String filename, HttpServletResponse response) {
        String filePath = Paths.get(uploadDir, filename).toString();

        // 检查文件是否存在
        if (!Files.exists(Paths.get(filePath))) {
            throw new RuntimeException("文件不存在！");
        }

        try (OutputStream os = response.getOutputStream()) {
            // 设置响应头，支持文件下载
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            response.setContentType("application/octet-stream");

            // 读取文件并写入响应
            byte[] bytes = FileUtil.readBytes(filePath);
            os.write(bytes);
            os.flush();
        } catch (IOException e) {
            throw new RuntimeException("文件下载失败！", e);
        }
    }
}
