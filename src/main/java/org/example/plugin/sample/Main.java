package org.example.plugin.sample;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class Main extends JavaPlugin implements Listener {

    // public private ← アクセス修飾子
    private int count;


    @Override
    public void onEnable() {
        saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("setlevel").setExecutor(new SetLevelCommand(this));
        getCommand("allsetlevel").setExecutor(new AllSetLevelCommand());

        // 例外処理
        //Exception //検査例外
        //RuntimeException // 非検査例外
        //Error //エラー

        //Exception exp = new Exception();
        //RuntimeException exp2 = new RuntimeException();


    }

    /**
     * プレイヤーがスニークを開始/終了する際に起動されるイベントハンドラ。
     *
     * @param e イベント
     */
    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e) throws IOException {
        // void ←処理を返さない　　PlayerToggleSneakEvent　←引数　　e ←変数

        // イベント発生時のプレイヤーやワールドなどの情報を変数に持つ。
        Player player = e.getPlayer();
        //     ↑変数 変更する時はリファクタリング
        World world = player.getWorld();


        List<Color> colorList = List.of(Color.RED, Color.BLUE, Color.WHITE, Color.BLACK);
        if (count % 2 == 0) {
            for (Color color : colorList) {

                // 花火オブジェクトをプレイヤーのロケーション地点に対して出現させる。
                Firework firework = world.spawn(player.getLocation(), Firework.class);

                // 花火オブジェクトが持つメタ情報を取得。
                FireworkMeta fireworkMeta = firework.getFireworkMeta();

                // メタ情報に対して設定を追加したり、値の上書きを行う。
                // 今回は青色で星型の花火を打ち上げる。
                fireworkMeta.addEffect(
                        FireworkEffect.builder()
                                .withColor(color)
                                .with(Type.BALL_LARGE)
                                .withFlicker()
                                .build());
                fireworkMeta.setPower(1);


                // 追加した情報で再設定する。
                firework.setFireworkMeta(fireworkMeta);
            }
        }
        count++;
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent e) {
        Player player = e.getPlayer();
        ItemStack[] itemStacks = player.getInventory().getContents();
        Arrays.stream(itemStacks)
                .filter(item -> !Objects.isNull(item) && item.getMaxStackSize() == 64 && item.getAmount() < 64)
                .forEach(item -> item.setAmount(64));

        player.getInventory().setContents(itemStacks);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String s = "プレイヤーが動きました！";
        player.sendMessage(s);
    }
}
