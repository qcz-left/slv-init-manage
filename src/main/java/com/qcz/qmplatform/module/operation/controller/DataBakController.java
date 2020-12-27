package com.qcz.qmplatform.module.operation.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qcz.qmplatform.common.aop.annotation.Module;
import com.qcz.qmplatform.common.aop.annotation.RecordLog;
import com.qcz.qmplatform.common.aop.assist.OperateType;
import com.qcz.qmplatform.common.bean.PageRequest;
import com.qcz.qmplatform.common.bean.PageResult;
import com.qcz.qmplatform.common.bean.PageResultHelper;
import com.qcz.qmplatform.common.bean.PrivCode;
import com.qcz.qmplatform.common.bean.ResponseResult;
import com.qcz.qmplatform.common.utils.StringUtils;
import com.qcz.qmplatform.module.base.BaseController;
import com.qcz.qmplatform.module.operation.domain.DataBak;
import com.qcz.qmplatform.module.operation.service.DataBakService;
import com.qcz.qmplatform.module.operation.vo.DataBakStrategyVO;
import com.qcz.qmplatform.module.operation.vo.DataBakVO;
import com.qcz.qmplatform.module.system.service.IniService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Map;

/**
 * <p>
 * 数据备份 前端控制器
 * </p>
 *
 * @author quchangzhong
 * @since 2020-12-26
 */
@Controller
@RequestMapping("/operation/data-bak")
@Module("数据备份与恢复")
public class DataBakController extends BaseController {

    private static final String PREFIX = "/module/operation/";

    /**
     * sys_ini配置属性组
     */
    private static final String SECTION = "DataBak";

    @Autowired
    private DataBakService dataBakService;

    @Autowired
    private IniService iniService;

    @GetMapping("/dataBakListPage")
    public String dataBakListPage() {
        return PREFIX + "dataBakList";
    }

    @GetMapping("/dataBakStrategyPage")
    public String dataBakStrategyPage() {
        return PREFIX + "dataBakStrategy";
    }

    @GetMapping("/getDataBakList")
    @ResponseBody
    public ResponseResult<PageResult> getDataBakList(PageRequest pageRequest, DataBakVO dataBakVO) {
        PageResultHelper.startPage(pageRequest);
        QueryWrapper<DataBak> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(dataBakVO.getBakName()), "bak_name", dataBakVO.getBakName());
        return ResponseResult.ok(PageResultHelper.parseResult(dataBakService.list(queryWrapper)));
    }

    @GetMapping("/getDataBakStrategy")
    @ResponseBody
    public ResponseResult<DataBakStrategyVO> getDataBakStrategy() {
        DataBakStrategyVO strategy = new DataBakStrategyVO();
        Map<String, String> iniPro = iniService.getBySec(SECTION);
        if (CollectionUtil.isNotEmpty(iniPro)) {
            strategy.setEnable(Integer.parseInt(iniPro.get("EnableBak")));
            int period = Integer.parseInt(iniPro.get("Period"));
            strategy.setPeriod(period);
            strategy.setWeek1(period & 1);
            strategy.setWeek2(period & 2);
            strategy.setWeek3(period & 4);
            strategy.setWeek4(period & 8);
            strategy.setWeek5(period & 16);
            strategy.setWeek6(period & 32);
            strategy.setWeek7(period & 64);
            strategy.setLimitDiskSpace(Integer.parseInt(iniPro.get("LimitDiskSpace")));
        }
        return ResponseResult.ok(strategy);
    }

    /**
     * 保存备份策略
     */
    @PostMapping("/saveDataBakStrategy")
    @ResponseBody
    @RequiresPermissions(PrivCode.BTN_CODE_DATA_BAK_STRATEGY_SAVE)
    @RecordLog(type = OperateType.UPDATE, description = "调整数据备份策略")
    public ResponseResult<?> saveDataBakStrategy(@RequestBody DataBakStrategyVO dataBakStrategyVO) {
        if (dataBakService.saveDataBakStrategy(dataBakStrategyVO)) {
            return ResponseResult.ok();
        }
        return ResponseResult.error();
    }

    /**
     * 执行备份操作
     */
    @PostMapping("/exeBackup")
    @ResponseBody
    @RequiresPermissions(PrivCode.BTN_CODE_DATA_BAK_SAVE)
    @RecordLog(type = OperateType.INSERT, description = "立即备份")
    public ResponseResult<?> exeBackup() {
        return dataBakService.exeBackup();
    }

    @DeleteMapping("/deleteDataBak")
    @ResponseBody
    @RequiresPermissions(PrivCode.BTN_CODE_DATA_BAK_DELETE)
    @RecordLog(type = OperateType.DELETE, description = "删除备份")
    public ResponseResult<?> deleteDataBak(String dataBakIds) {
        if (dataBakService.deleteDataBak(Arrays.asList(dataBakIds.split(",")))) {
            return ResponseResult.ok();
        }
        return ResponseResult.error();
    }
}
