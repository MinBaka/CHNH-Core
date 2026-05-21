ClientEvents.tick(event => {
    if (!Platform.isLoaded("parcool")) return;
    let player = event.player;
    if (player == null || SkpUtils.commonUtils.isScreenOpen()) return;

    // 动作判定条件逻辑：
    let isMoving = player.isSprinting() || player.isCrouching() || !player.onGround() || player.getDeltaMovement().lengthSqr() > 0.001;

    // 1. 当玩家在奔跑时，提示 FastRun / Dodge / Slide
    if (player.isSprinting()) {
        SkpUtils.promptUtils.show("parcool", "key.parcool.FastRun");
        SkpUtils.promptUtils.show("parcool", "key.parcool.Dodge");
        SkpUtils.promptUtils.show("parcool", "key.parcool.Crawl"); // 滑铲
    }

    // 2. 当玩家在下落时，提示 Breakfall (翻滚受身) 和 攀爬/悬挂
    if (player.fallDistance > 1.5) {
        SkpUtils.promptUtils.show("parcool", "key.parcool.Breakfall");
        SkpUtils.promptUtils.show("parcool", "key.parcool.ClingToCliff");
    }

    // 3. 当玩家处于离地状态(可能是贴墙)，提示 墙跳/抓边
    if (!player.onGround() && player.fallDistance <= 1.5) {
        SkpUtils.promptUtils.show("parcool", "key.parcool.WallJump");
        SkpUtils.promptUtils.show("parcool", "key.parcool.HangDown");
    }

    // 4. 平时潜行时提示 Crawl 和 翻滚
    if (player.isCrouching()) {
        SkpUtils.promptUtils.show("parcool", "key.parcool.Crawl");
        SkpUtils.promptUtils.show("parcool", "key.parcool.Flipping");
    }

    // 5. 如果只是普通移动且在地面上
    if (player.onGround() && !player.isSprinting() && !player.isCrouching() && player.getDeltaMovement().lengthSqr() > 0.001) {
        SkpUtils.promptUtils.show("parcool", "key.parcool.Vault");
        SkpUtils.promptUtils.show("parcool", "key.parcool.ClimbPoles");
    }
});
