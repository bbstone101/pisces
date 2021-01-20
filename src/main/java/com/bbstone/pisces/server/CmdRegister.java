package com.bbstone.pisces.server;

import com.bbstone.pisces.server.cmd.CmdHandler;
import com.bbstone.pisces.comm.BFileCmd;
import com.bbstone.pisces.server.cmd.FileReqHandler;
import com.bbstone.pisces.server.cmd.ListReqHandler;

import java.util.HashMap;
import java.util.Map;

public class CmdRegister {

    private static final Map<String, CmdHandler> cmdHandlerMap = new HashMap<>();

    public static void init() {
        register(BFileCmd.REQ_FILE, new FileReqHandler());
        register(BFileCmd.REQ_LIST, new ListReqHandler());


    }

    public static void register(String cmd, CmdHandler cmdHandler) {
        cmdHandlerMap.put(cmd, cmdHandler);
    }

    public static CmdHandler getHandler(String cmd) {
        return cmdHandlerMap.get(cmd);
    }

}
