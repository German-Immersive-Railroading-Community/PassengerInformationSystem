function search(event) {
	if (event.code === "Enter") {
		var search = event.explicitOriginalTarget.value
		if (search.trim().length !== 0) {
			search = search.replace(' ', '+');
			window.location.href = "/search?query=" + search;
		}
	}
}

function startRefresh() {
	$.get('', function(data) {
		$(document.body).html(data);    
	});
}

function reload() {
	$(function() {
		setTimeout(startRefresh, 10000);
	});
}

function addStation() {
	const stations = document.getElementById("stations").getElementsByTagName("tbody")[0];
	const tabeRows = stations.getElementsByTagName("tr");
	const newStation = document.getElementById("template").cloneNode(true);
	newStation.removeAttribute("id");
	stations.appendChild(newStation);
	changeIndex(tabeRows.item(tabeRows.length - 1), tabeRows.length - 3);
}

function removeStation(index) {
	console.log(index)
	const stations = document.getElementById("stations").getElementsByTagName("tbody")[0];
	let childToRemove;
	let alreadyRemoved;
	Array.from(stations.children).forEach(element => {
		if (element.getAttribute("index") != null) {
			if (element.getAttribute("index") == index) {
				childToRemove = element;
				alreadyRemoved = true;
			} else if (alreadyRemoved) {
				changeIndex(element, element.getAttribute("index") - 1)
			}
		}
	});
	stations.removeChild(childToRemove);
}

function removeTemplate() {
	document.getElementById("template").remove();
}

function changeIndex(element, newIndex) {
	const oldIndex = element.getAttribute("index");
	element.setAttribute("index", newIndex);
	const inputs = element.getElementsByTagName("input");
	const selects = element.getElementsByTagName("select");
	const remove = element.getElementsByTagName("a")[0];
	Array.from(inputs).forEach(elements => {
		elements.setAttribute("id", elements.getAttribute("id").replace(oldIndex, newIndex));
		elements.setAttribute("name", elements.getAttribute("name").replace(oldIndex, newIndex));
	});
		Array.from(selects).forEach(elements => {
		elements.setAttribute("id", elements.getAttribute("id").replace(oldIndex, newIndex));
		elements.setAttribute("name", elements.getAttribute("name").replace(oldIndex, newIndex));
	});
	remove.setAttribute("onclick", remove.getAttribute("onclick").replace(oldIndex, newIndex));
}

/* --- */

document.addEventListener('readystatechange', event => { 
    if (event.target.readyState === "interactive") {onLoad();}
    if (event.target.readyState === "complete") {}
});

//This function will be called, if the body does load the first time
function onLoad() {
    const navigation = document.getElementById("navigation")
    const menu = document.getElementById("smart-menu");
    if (navigation && navigation.classList.contains("navigation-headlines")) generateNavigation();
    if (menu) initializeSmartMenu();
}

//Remove all elements of menu from the viewport except for the title and images
function initializeSmartMenu() {
    const menu = document.getElementById("smart-menu");
    const navigation = document.getElementById("navigation");
    if (menu) {
        document.addEventListener("click", onMouseClick);
        window.addEventListener("resize", onWindowResize);
        Array.from(menu.children).forEach(element => {
            if (!element.classList.contains("menu-title") && !(element.tagName === "IMG")) {
                element.classList.add("not-mobile");
            }
        });
    }
    if (!navigation) menu.outerHTML += "<div id=\"navigation\" class=\"navigation only-mobile\"></div>";
}

//Generate a link to a h2 headline at the bottom of the navigation
function generateNavigation() {
    const navigation = document.getElementById("navigation");
    if (navigation) {
        Array.from(document.getElementsByTagName("h2")).forEach(element => {
            if (typeof element.id != 'undefined' && !(element.id === '')) {
                navigation.innerHTML += "<a href=\"#" + element.id + "\">" + element.innerHTML + "</a>";
            }
        });
    }
}

//Toggle the navigation for the menu on mobile devices
function toggleSmartMenu() {
    const navigation = document.getElementById("navigation");
    let navigationContent = document.getElementsByClassName("navigation-content").item(0);
    if (document.getElementsByClassName("container-fluid").item(0)) navigationContent=document.getElementsByClassName("container-fluid").item(0);
    else if (!navigationContent && document.getElementsByClassName("container").item(0)) navigationContent=document.getElementsByClassName("container").item(0);
    if (navigation) {
        if (navigation.classList.contains("navigation-display")) {
            navigation.classList.remove("navigation-display");
            if (navigationContent) navigationContent.classList.remove("navigation-content-hide");
            Array.from(navigation.children).forEach(element => {
                if (element.classList.contains("menu-item")) navigation.removeChild(element); 
            });
        } else {
            const menu = document.getElementById("smart-menu");
            generateNavigationMenu(menu, navigation);
            navigation.classList.add("navigation-display");
            if (navigationContent) navigationContent.classList.add("navigation-content-hide");
        }
        
    }
}

//Focus the element in the menu, which has the innerHtml of string
function setMenuFocus(string) {
    let menu = document.getElementsByClassName("menu").item(0);
    setRecursiveMenuFocus(string, menu);
}

//Focus the element in the navigation, which has the innerHtml of string
function setNavigationFocus(string) {
    let navigation = document.getElementsByClassName("navigation").item(0);
    setRecursiveNavigationFocus(string, navigation);
}

//private
function setRecursiveMenuFocus(string, menu) {
    if (menu && Array.from(menu.children).length > 0) {
        Array.from(menu.children).forEach(element => {
            if (Array.from(element.children).length > 0) {
                setRecursiveMenuFocus(string, element);
            } else if (element.innerHTML.toLowerCase() === string.toLowerCase()) {
                element.classList.add("menu-active");
                return;
            }
        });
    }
}

//private
function setRecursiveNavigationFocus(string, navigation) {
    if (navigation && Array.from(navigation.children).length > 0) {
        Array.from(navigation.children).forEach(element => {
            if (Array.from(element.children).length > 0) {
                setRecursiveMenuFocus(string, element);
            } else if (element.innerHTML.toLowerCase() === string.toLowerCase()) {
                element.classList.add("navigation-active");
                return;
            }
        });
    }
}

// private
function onMouseClick(event) {
    const navigation = document.getElementById("navigation");
    if (navigation) {
        if (navigation.classList.contains("navigation-display") && event.clientX > 250) {
            toggleSmartMenu();
        }
    }
}

//private
function onWindowResize() {
    const navigation = document.getElementById("navigation");
    if (navigation) {
        if (navigation.classList.contains("navigation-display")) {
            toggleSmartMenu();
        }
    }
}

//private
function generateNavigationMenu(menu, navigation) {
    if (menu && navigation) {
        let menuString = "";
        let hasHome = false;
        Array.from(menu.children).forEach(element => {
            if (!hasHome && navigation && element.tagName === "A" && element.innerHTML === "Home") hasHome = true;
        });
        Array.from(menu.children).forEach(element => {
            if (navigation && element.tagName === "A") {
                let innerHtml = element.innerHTML;
                if (element.classList.contains("menu-title")) innerHtml = "Home";
                let classString = "only-mobile menu-item ";
                if (element.classList.contains("menu-active")) classString += "navigation-active";
                if (!element.classList.contains("menu-title") || !hasHome) menuString += "<a href=\"" + element.href + "\" class=\"" + classString + "\">" + innerHtml + "</a>";
            } else if (navigation && Array.from(element.children).length > 0) {
                generateNavigationMenu(element, navigation);
            }
        });
        navigation.innerHTML = menuString + navigation.innerHTML;
    }
}