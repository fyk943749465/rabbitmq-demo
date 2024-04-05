package btc.data;

import org.bitcoinj.core.Block;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.utils.BlockFileLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class resolve {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        String filePath = "E:\\blk00000.dat";

        List<File> blockChainFiles = new ArrayList<>();
        blockChainFiles.add(new File(filePath));
        MainNetParams params = MainNetParams.get();
        BlockFileLoader bfl = new BlockFileLoader(params, blockChainFiles);

        while (bfl.hasNext()) {

            Block block = bfl.next();
            System.out.println(block.getHash());

        }
    }
}
