package me.chang.gpms.pojo.ro;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "文件名请求对象", name = "FileNameRo")
@Data
public class FileNameRo {
    @Schema(description = "文件名", example = "这里是文件名", name = "FileName")
    private String fileName;
}
