package com.lotto.lotto_simulator.service;

import com.lotto.lotto_simulator.controller.requestDto.LottoCombinationDto;
import com.lotto.lotto_simulator.controller.requestDto.LottoDto;
import com.lotto.lotto_simulator.controller.requestDto.TestLottoDto;
import com.lotto.lotto_simulator.controller.requestDto.UniqueCodeDto;
import com.lotto.lotto_simulator.controller.responseDto.*;
import com.lotto.lotto_simulator.entity.*;
import com.lotto.lotto_simulator.repository.lottorepository.JdbcLottoRepository;
import com.lotto.lotto_simulator.repository.lottocombinationrepository.LottoCombinationRepository;
import com.lotto.lotto_simulator.repository.lottorepository.JdbcTestRepository;
import com.lotto.lotto_simulator.repository.lottorepository.LottoRepository;
import com.lotto.lotto_simulator.repository.roundrepository.RoundRepository;
import com.lotto.lotto_simulator.repository.storerpository.StoreRepository;
import com.lotto.lotto_simulator.repository.testlottorepository.TestLottoRepository;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class LottoService {
    private final LottoRepository lottoRepository;
    private final StoreRepository storeRepository;
    private final RoundRepository roundRepository;
    private final LottoCombinationRepository lottoCombinationRepository;
    private final JdbcLottoRepository jdbcLottoRepository;
    private final JdbcTestRepository jdbcTestRepository;
    private final TestLottoRepository testLottoRepository;



    @Transactional(readOnly = true)
    public ResponseDto<?> winningNum(Pageable pageable) {
        Page<Round> winningNum = roundRepository.pageable(pageable);
        return ResponseDto.success(winningNum);
    }

    //feature/uniqueCodeSearch
    @Transactional(readOnly = true)
    public ResponseDto<?> lottoInfo(Long num, UniqueCodeDto uniqueIdDto) {

        // 매개변수로 들어온 유니크 코드를 가지고 있는 Lotto 전부 가져오기
        List<LottoDto> lottoList = lottoRepository.uniqueCodeSearch(uniqueIdDto.getUniqueCode());

        // num라운드의 당첨번호 정보를 가져온다.
        Round round = roundRepository.findByRound(num).orElseThrow();
        List<Byte> rounds = new ArrayList<>();
        rounds.add(round.getNum1());
        rounds.add(round.getNum2());
        rounds.add(round.getNum3());
        rounds.add(round.getNum4());
        rounds.add(round.getNum5());
        rounds.add(round.getNum6());


        List<List<Byte>> singleLottoNum = new ArrayList<>();

        for (LottoDto l : lottoList) {
            List<Byte> lottoNum = new ArrayList<>();
            lottoNum.add(l.getFirstNum());
            lottoNum.add(l.getSecondNum());
            lottoNum.add(l.getThirdNum());
            lottoNum.add(l.getFourthNum());
            lottoNum.add(l.getFifthNum());
            lottoNum.add(l.getSixthNum());
            singleLottoNum.add(lottoNum);
        }

        int firstRank = 0;
        int secondRank = 0;
        int thirdRank = 0;
        int fourthRank = 0;
        int fifthRank = 0;

        // 등수와 당첨번호를 모아넣는 Map
        HashMap<Integer, List<List<Byte>>> winLottoMap = new HashMap<>();
        List<List<Byte>> firstList = new ArrayList<>();
        List<List<Byte>> secondList = new ArrayList<>();
        List<List<Byte>> thirdList = new ArrayList<>();
        List<List<Byte>> fourthList = new ArrayList<>();
        List<List<Byte>> fifthList = new ArrayList<>();

        for (List<Byte> l : singleLottoNum) {

            HashMap<Byte, Integer> map = new HashMap<>();
            for (Byte value : rounds) {
                map.put(value, map.getOrDefault(value, 0) + 1);
            }
            for (Byte aByte : l) {
                map.put(aByte, map.getOrDefault(aByte, 0) - 1);
            }


            int cnt = 0;
            for (Byte key : map.keySet()) {
                if (map.get(key) > 0) {
                    cnt++;
                }
            }
            if (cnt == 0) {
                firstRank++;
                firstList.add(l);
            } else if (cnt == 1 && l.contains(round.getBonus())) {
                secondRank++;
                secondList.add(l);
            } else if (cnt == 1) {
                thirdRank++;
                thirdList.add(l);
            } else if (cnt == 2) {
                fourthRank++;
                fourthList.add(l);
            } else if (cnt == 3) {
                fifthRank++;
                fifthList.add(l);
                System.out.println("5등 = " + l);
            }

        }

        winLottoMap.put(1, firstList);
        winLottoMap.put(2, secondList);
        winLottoMap.put(3, thirdList);
        winLottoMap.put(4, fourthList);
        winLottoMap.put(5, fifthList);

//        System.out.println(winLottoMap);

//        RankResponseDto rankResponseDto = RankResponseDto.builder()
//                .firstRank(firstRank)
//                .secondRank(secondRank)
//                .thirdRank(thirdRank)
//                .fourthRank(fourthRank)
//                .fifthRank(fifthRank)
//                .build();

        return ResponseDto.success(winLottoMap);
    }

    // 등수를 구하는 새로운 로직 Test
    @Transactional
    public ResponseDto<?> test(Long num) {

        String uniqueCode = UUID.randomUUID().toString();

        List<Store> stores = storeRepository.searchAll();

        // 랜덤 로또 번호 6개를 저장하고 있는 List
        List<Integer> ranNumList;

        // 1 ~ 45부터 당첨번호가 무엇인지 모아놓음
        StringBuilder lottoList;

        // Batch Insert를 위한 List
        List<TestLotto> batchInsertList = new ArrayList<>();

        for(int i = 0 ; i < num; i++){

            // for문 한 번 돌릴 때마다 초기화
            lottoList = new StringBuilder();


            ranNumList = new ArrayList<Integer>();

            // 랜덤 로또 번호 6개 생성
            while(ranNumList.size() < 6){
                int n = (int) ((Math.random() * 45) + 1);
                if(ranNumList.contains(n))
                    continue;
                else
                    ranNumList.add(n);
            }

            System.out.println(ranNumList);

            // 랜덤 로또 번호 6개 생성한 부분은 "1" 아닌 부분은 "0"을 append()
            for(int j = 0; j < 45; j++){
                if(ranNumList.contains(j + 1)){
                    lottoList.append("1");
                }
                else
                    lottoList.append("0");
            }

            TestLotto testLotto = TestLotto.builder()
                    .lottoNum(lottoList.toString())
                    .uniqueCode(uniqueCode)
                    .store(stores.get((int) (Math.random() * stores.size())))
                    .build();

            batchInsertList.add(testLotto);
        }

        jdbcTestRepository.batchInsertLottos(batchInsertList);


        return ResponseDto.success("success");
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getTest(Long num) {

        Round round = roundRepository.findByRound(num).orElseThrow();

        // 회차의 당첨번호만 모아놓은 List
        List<Byte> rounds = new ArrayList<>();
        rounds.add(round.getNum1());
        rounds.add(round.getNum2());
        rounds.add(round.getNum3());
        rounds.add(round.getNum4());
        rounds.add(round.getNum5());
        rounds.add(round.getNum6());

        int firstRank = 0;
        int secondRank = 0;
        int thirdRank = 0;
        int fourthRank = 0;
        int fifthRank = 0;

        // TestLotto 데이터 전부 다 들고오기
        List<TestLottoDto> testLottoDtoList = testLottoRepository.searchAll();

        // ?
        List<String[]> testLottoDtoListAll = new ArrayList<>();

        // TestLotto의 로또 번호가 총 몇 개가 맞는지 세어주는 변수
        int cnt = 0;

        // TestLotto의 개수 만큼 for문 돌기
        for(int i = 0 ; i < testLottoDtoList.size(); i++){

            cnt = 0;

            for(int j = 0; j < rounds.size(); j++){
                if(testLottoDtoList.get(i).getLottoNum().charAt(rounds.get(j) - 1) == '1')
                    cnt++;
            }

            if(cnt == 6) {
                firstRank++;
                //System.out.println(testLottoDtoList.get(i) + "  1등 =====================================================");
            }
            else if(cnt == 5 && testLottoDtoList.get(i).getLottoNum().contains(round.getBonus().toString())) {
                secondRank++;
                //System.out.println(testLottoDtoList.get(i) + "  2등 =====================================================");
            }
            else if(cnt == 5) {
                thirdRank++;
                //System.out.println(testLottoDtoList.get(i) + "  3등 =====================================================");
            }
            else if(cnt == 4) {
                fourthRank++;
                //System.out.println(testLottoDtoList.get(i) + "  4등 =====================================================");
            }
            else if(cnt == 3) {
                fifthRank++;
                //System.out.println(testLottoDtoList.get(i) + "  5등 =====================================================");
            }
        }
// 010000 -> 2번이지만 인덱스는 1이다 -> 1, 2

//        System.out.println("1등 = " + firstRank + "================================================================");
//        System.out.println("2등 = " + secondRank + "================================================================");
//        System.out.println("3등 = " + thirdRank + "================================================================");
//        System.out.println("4등 = " + fourthRank + "================================================================");
//        System.out.println("5등 = " + fifthRank + "================================================================");


        return ResponseDto.success("success");
    }
}

// 10 23 29 33 37 40      16