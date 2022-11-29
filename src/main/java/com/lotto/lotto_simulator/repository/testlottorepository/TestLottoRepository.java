package com.lotto.lotto_simulator.repository.testlottorepository;

import com.lotto.lotto_simulator.entity.TestLotto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestLottoRepository extends JpaRepository<TestLotto, Long>, TestLottoRepositoryCustom {
}
