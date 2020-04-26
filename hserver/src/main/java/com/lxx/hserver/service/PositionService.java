package com.lxx.hserver.service;

import com.lxx.hserver.entity.Position;
import com.lxx.hserver.mapper.PositionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class PositionService {
    @Autowired
    PositionMapper positionMapper;
    public List<Position> getAllPos() {
        return positionMapper.getAllPos();
    }
    public List<Position> getPos(){
        return positionMapper.getPos();
    }

    public int addPos(String posName) {
        Position position=positionMapper.getPosByName(posName);
        if (StringUtils.isEmpty(position)){
            Position newPos=new Position();
            newPos.setName(posName);
            newPos.setCreateDate(new Date());
            newPos.setEnabled(true);
           return positionMapper.insertPos(newPos);
        }
        return 0;
    }

    public int disabledPos(Position position ) {
        position.setEnabled(!position.getEnabled());
        return positionMapper.disabledPos(position);
    }
    @Transactional(rollbackFor = RuntimeException.class)
    public int updatePos(Position position) {
        if (StringUtils.isEmpty(position.getName())){
            throw new RuntimeException("职位名不能为空!");
        }
        if (StringUtils.isEmpty(positionMapper.getPosByName(position.getName()))){
            return positionMapper.updatePos(position);
        }
        return 0;
    }
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean deletePosByIds(String ids) {
        String[] id=ids.split(",");
         if (positionMapper.deletePosById(id)==id.length){
            return true;
         }else {
             throw new RuntimeException("删除失败！");
         }
    }
}
