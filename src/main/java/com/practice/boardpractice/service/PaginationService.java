package com.practice.boardpractice.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class PaginationService {

    private static final int BAR_LENGTH = 5;


    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int totalPages) {
        // 페이지 표시 부분. ex) 1 2 3 4 5.
        // 시작 페이지는 현재 페이지 에서 2칸 앞으로. 만약 현재 페이지가 1페이지면 마이너스 페이지가 되니까 Math.max로 0페이지가 되도록.
        int startNumber = Math.max(currentPageNumber - (BAR_LENGTH / 2), 0);
        // 마지막 페이지인 5 페이지는 시작 페이지에 + 5를 함. 마지막 페이지 수를 넘어가지 않도록. Math.min으로 작은걸 가져옴.
        int endNumber = Math.min(startNumber + BAR_LENGTH, totalPages);

        return IntStream.range(startNumber, endNumber).boxed().toList(); // 범위로 Integer List로 생성해서 return.
        // p.s. - 첫번쨰 파라미터는 포함. 두번째는 미포함. 1st parameter is inclusive. 2nd parameter is exclusive.
        // ex) IntStream.range(0, 5) => 0,1,2,3,4 까지만 생성.
    }

    public int currentBarLength() {
        return BAR_LENGTH;
    }
}
