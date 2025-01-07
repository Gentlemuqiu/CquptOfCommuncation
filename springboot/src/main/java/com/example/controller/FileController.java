package com.example.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.example.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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

        // 文件名和路径安全处理
        String originalFilename = file.getOriginalFilename();
        String flag = IdUtil.fastSimpleUUID(); // 生成唯一标识
        String safeFilename = originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_"); // 过滤非法字符
        String filePath = Paths.get(uploadDir, flag + "_" + safeFilename).toString();

        // 写入文件
        FileUtil.writeBytes(file.getBytes(), filePath);

        // 返回文件访问 URL
        String fileUrl = "http://" + ip + ":" + port + "/files/" + flag + "_" + safeFilename;
        return Result.success(fileUrl);
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
