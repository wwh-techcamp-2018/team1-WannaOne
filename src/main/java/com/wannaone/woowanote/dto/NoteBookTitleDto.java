package com.wannaone.woowanote.dto;

import com.wannaone.woowanote.domain.NoteBook;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteBookTitleDto {
    @NotBlank
    @Size(max = 30)
    private String title;
    public NoteBook toEntity(){
        return new NoteBook(this.title);
    }
}
