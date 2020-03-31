package com.qing.tea.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
public class Storage {
    /*
   仓储地点
   */
    @Field("place")
    private String place;

    /*
    开始日期
  */
    @Field("startDate")
    private Date startDate;

    /*
      结束日期
      */
    @Field("endDate")
    private Date endDate;

    /*
      仓储负责人
      */
    @Field("storageer")
    private String storageer;
}
