package com.bbstone.pisces.client;

import com.bbstone.pisces.client.cmd.CmdHandler;
import com.bbstone.pisces.client.cmd.DirRspHandler;
import com.bbstone.pisces.client.cmd.FileRspHandler;
import com.bbstone.pisces.client.cmd.ListRspHandler;
import com.bbstone.pisces.comm.BFileCmd;

import java.util.HashMap;
import java.util.Map;

public class ClientCmdRegister {

    private static final Map<String, CmdHandler> cmdHandlerMap = new HashMap<>();

    public static void init() {
        register(BFileCmd.RSP_FILE, new FileRspHandler());
        register(BFileCmd.RSP_DIR, new DirRspHandler());
        register(BFileCmd.RSP_LIST, new ListRspHandler());

    }

    public static void register(String cmd, CmdHandler cmdHandler) {
        cmdHandlerMap.put(cmd, cmdHandler);
    }

    public static CmdHandler getHandler(String cmd) {
        return cmdHandlerMap.get(cmd);
    }

}
