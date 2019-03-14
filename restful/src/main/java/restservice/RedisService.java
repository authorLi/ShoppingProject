package restservice;

import utils.TaotaoResult;

public interface RedisService {

    TaotaoResult syncContent(long contentCid);
}
