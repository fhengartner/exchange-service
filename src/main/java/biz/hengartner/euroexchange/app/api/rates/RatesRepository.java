package biz.hengartner.euroexchange.app.api.rates;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Transactional
public interface RatesRepository extends JpaRepository<Rate, Long> {

    Rate findByCurrencyAndDate(@Param("currency") String currency, @Param("date") LocalDate date);

    @Query(value = "select currency from Rate group by currency",
            countQuery = "select count(1) from (select count(1) from Rate group by currency)")
    public List<String> currencies();
}
