package com.lotto.lotto_simulator.controller.requestDto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TestLottoDto {

    // 문자열로 된 로또 넘버들
    private String lottoNum;

    @QueryProjection
    public TestLottoDto(String lottoNum) {
        this.lottoNum = lottoNum;
    }
}
