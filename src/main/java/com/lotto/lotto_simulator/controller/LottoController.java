package com.lotto.lotto_simulator.controller;

import com.lotto.lotto_simulator.controller.requestDto.LottoDto;
import com.lotto.lotto_simulator.controller.requestDto.UniqueCodeDto;
import com.lotto.lotto_simulator.controller.responseDto.ResponseDto;
import com.lotto.lotto_simulator.service.LottoService;
import com.lotto.lotto_simulator.service.PurchaseService;
import com.lotto.lotto_simulator.service.RoundService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class LottoController {
    private final LottoService lottoService;

    //feature/uniqueCodeSearch
    @CrossOrigin
    @GetMapping("/lottos/info/{num}")
    public ResponseDto<?> lottoInfo(@PathVariable Long num,
                                    @RequestBody UniqueCodeDto uniqueIdDto) {
        return lottoService.lottoInfo(num, uniqueIdDto);
    }

    @CrossOrigin
    @PostMapping("/test/{num}")
    public ResponseDto<?> test(@PathVariable Long num){
        return lottoService.test(num);
    }

    @CrossOrigin
    @GetMapping("/test/{num}")
    public ResponseDto<?> getTest(@PathVariable Long num){
        return lottoService.getTest(num);
    }
}
