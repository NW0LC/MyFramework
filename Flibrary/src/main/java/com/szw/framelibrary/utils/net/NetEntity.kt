package com.szw.framelibrary.utils.net

//@HttpResponse(parser = JsonResponseParser.class)//每一个实体类必须加这句话，别忘了在baseactivity里面 初始化注解 x.view().inject(this);
class NetEntity<T> {
    //    "code":"200",
    //            "message":"提示"，
    //            "data": "1234"

    private var code: String=""
    var message: String=""
    var data: T? = null
    var info: T? = null

    fun getCode(): Int = Integer.valueOf(code)

    fun setCode(code: String) {
        this.code = code
    }
}
