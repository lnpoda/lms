package com.example.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class LoanApplicationReviewDto {

    private LoanApplicationRequestDto loanApplicationRequestDto;

    private LoanApplicationResponseDto loanApplicationResponseDto;
}
