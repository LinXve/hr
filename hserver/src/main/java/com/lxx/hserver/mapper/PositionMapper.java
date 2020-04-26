package com.lxx.hserver.mapper;

import com.lxx.hserver.entity.Position;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PositionMapper {

    List<Position> getAllPos();
    List<Position> getPos();

    Position getPosByName(@Param("name") String name);

    int insertPos(@Param("pos") Position position);

    int disabledPos(@Param("pos") Position position );

    int updatePos(@Param("pos") Position position);

    int deletePosById(@Param("ids") String[] ids);
}
