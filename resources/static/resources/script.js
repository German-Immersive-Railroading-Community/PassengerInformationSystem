document.addEventListener('readystatechange', event => { 
    if (event.target.readyState === "interactive") {
		if (document.getElementById("reload") != null) {
			setInterval(function() {
				$('#reload').replaceWith($("#reload").load(document.URL +  " #reload > *"));
			}, 15000);
		}
	}
})

function search(event) {
	if (event.code === "Enter") {
		var search = event.explicitOriginalTarget.value
		if (search.trim().length !== 0) {
			search = search.replace(' ', '+');
			window.location.href = "/search?query=" + search;
		}
	}
}

function addStation() {
	const stations = document.getElementById("stations");
	const newStation = document.getElementById("template").cloneNode(true);
	newStation.removeAttribute("id");
	stations.appendChild(newStation);
	changeIndex(stations.children.item(stations.children.length - 1), stations.children.length - 3);
}

function removeStation(index) {
	const stations = document.getElementById("stations");
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