package io.metersphere.service;

import io.metersphere.base.domain.MinderExtraNode;
import io.metersphere.base.domain.MinderExtraNodeExample;
import io.metersphere.base.mapper.MinderExtraNodeMapper;
import io.metersphere.track.request.MinderExtraNodeEditRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class MinderExtraNodeService {

    @Resource
    MinderExtraNodeMapper minderExtraNodeMapper;

    public void batchEdit(MinderExtraNodeEditRequest request) {
        Map<String, List<String>> data = request.getData();
        if (data != null) {
            data.forEach((parentId, nodes) -> {
                nodes.forEach(node -> {
                    MinderExtraNode minderExtraNode = new MinderExtraNode();
                    minderExtraNode.setNodeData(node);
                    minderExtraNode.setParentId(parentId);
                    if (StringUtils.isBlank(request.getId()) || request.getId().length() < 20) {
                        minderExtraNode.setId(UUID.randomUUID().toString());
                        minderExtraNode.setGroupId(request.getGroupId());
                        minderExtraNode.setType(request.getType());
                        minderExtraNodeMapper.insert(minderExtraNode);
                    } else {
                        minderExtraNodeMapper.updateByPrimaryKeySelective(minderExtraNode);
                    }
                });
            });
        }
        List<String> ids = request.getIds();
        if (CollectionUtils.isNotEmpty(ids)) {
            MinderExtraNodeExample example = new MinderExtraNodeExample();
            example.createCriteria().andIdIn(ids);
            minderExtraNodeMapper.deleteByExample(example);
        }
    }

    public List<MinderExtraNode> selectByParentId(String parentId) {
        MinderExtraNodeExample example = new MinderExtraNodeExample();
        example.createCriteria().andParentIdEqualTo(parentId);
        return minderExtraNodeMapper.selectByExample(example);
    }
}