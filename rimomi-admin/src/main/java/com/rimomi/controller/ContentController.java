package com.rimomi.controller;

import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.dto.TageListDto;
import com.rimomi.domain.entity.Tag;
import com.rimomi.domain.vo.PageVo;
import com.rimomi.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tag/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TageListDto tageListDto){
        return tagService.getlist(pageNum, pageSize, tageListDto);
    }
    @PostMapping("/tag")
    public ResponseResult addTag(@RequestBody Tag tag){
        return tagService.addTag(tag);
    }
    @DeleteMapping("/tag/{id}")
    public ResponseResult deleteTag(@PathVariable Long id){
        return tagService.deleteTag(id);
    }
    @GetMapping("/tag/{id}")
    public ResponseResult getTag(@PathVariable Long id) {
        return tagService.getTag(id);
    }
    @PutMapping("/tag")
    public ResponseResult putTag(@RequestBody Tag tag){
        return tagService.putTag(tag);
    }
    @GetMapping("/category/listAllCategory")
    public ResponseResult listAllCategory(){
        return tagService.listAllCategory();
    }
    @GetMapping("/tag/listAllTag")
    public ResponseResult listAllTag(){
        return tagService.listAllTag();
    }
}
