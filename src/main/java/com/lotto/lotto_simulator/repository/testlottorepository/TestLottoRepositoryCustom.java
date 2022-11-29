package com.lotto.lotto_simulator.repository.testlottorepository;

import com.lotto.lotto_simulator.controller.requestDto.TestLottoDto;

import java.util.List;

public interface TestLottoRepositoryCustom {

    List<TestLottoDto> searchAll();
}
