package com.wannaone.woowanote.dto;

import com.wannaone.woowanote.domain.NoteBook;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteBookDto {
    @NotBlank
    private String title;
    public NoteBook toEntity(){
        return new NoteBook(this.title);
    }
}
