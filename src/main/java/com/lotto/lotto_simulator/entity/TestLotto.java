package com.lotto.lotto_simulator.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="testLotto",indexes = {
        @Index(name = "test_lotto_idx"
                , columnList = "lottoNum")
})
public class TestLotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Test_lotto_id;

    @Column(nullable = false)
    private String lottoNum;

    @Column(nullable = false)
    private String uniqueCode;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="store_id")
    private Store store;

}

