package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.dto.PayPriceDTO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PayPriceMapper {
    @Select("SELECT start_time, end_time From reservation WHERE reservation_date = #{date}")
    List<PayPriceDTO> findByDate(String date);
}
