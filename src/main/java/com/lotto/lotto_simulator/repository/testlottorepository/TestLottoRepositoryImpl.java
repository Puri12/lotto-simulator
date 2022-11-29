package com.lotto.lotto_simulator.repository.testlottorepository;

import com.lotto.lotto_simulator.controller.requestDto.QLottoDto;
import com.lotto.lotto_simulator.controller.requestDto.QTestLottoDto;
import com.lotto.lotto_simulator.controller.requestDto.TestLottoDto;
import com.lotto.lotto_simulator.entity.QTestLotto;
import com.lotto.lotto_simulator.entity.TestLotto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.lotto.lotto_simulator.entity.QTestLotto.testLotto;

@Repository
@RequiredArgsConstructor
public class TestLottoRepositoryImpl implements TestLottoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TestLottoDto> searchAll(){
        return queryFactory
                .select(new QTestLottoDto(testLotto.lottoNum))
                .from(testLotto)
                .fetch();
    }
}
