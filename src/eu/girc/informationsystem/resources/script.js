function setMenuFocus(string) {
    var menus = document.getElementsByClassName("menu");
    if (menus.length) {
        var menu = menus[0];
        for (var i = 0; i < menu.children.length; i++) {
            if (menu.children[i].id === "menu-" + string) {
                menu.children[i].classList.add("menu-active");
                return;
            }
        }
    }
}