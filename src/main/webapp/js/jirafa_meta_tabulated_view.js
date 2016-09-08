function linkIssuesToTest(identifier) {
  var issuesToLink = new Array();
  var inputElements = document.getElementsByName(identifier + '-issue[]');
  var test = document.getElementById(identifier + '-link-submit').dataset.test;
  var stacktrace = document.getElementById(identifier + '-link-submit').dataset.stacktrace;
  for(var i = 0; inputElements[i]; ++i){
    if(inputElements[i].checked){
      issuesToLink.push(inputElements[i].value);
    }
  }

  ajaxJirafa.linkIssuesToTest(test, issuesToLink, stacktrace, function (t) {
    location.reload(true);
  });
}

function deleteLinks(identifier) {
  var linksToDelete = new Array();
  var inputElements = document.getElementsByName(identifier + '-link[]');
  for(var i = 0; inputElements[i]; ++i){
    if(inputElements[i].checked){
      linksToDelete.push(inputElements[i].value);
    }
  }

  ajaxJirafa.deleteLinks(linksToDelete, function (t) {
    location.reload(true);
  });
}

function linkIssuesToAll() {
  var tests = new Array();
  var issueKeys = new Array();

  var boxes = document.getElementsByClassName('jirafa-box');
  for(var i = 0; boxes[i]; ++i) {
    var box = boxes[i];
    var boxId = box.id;

    var submitButton = document.getElementById(boxId + '-link-submit');
    if (submitButton == null) {
      continue;
    }

    var test = submitButton.dataset.test;
    tests.push(test);

    var issueKeysForTest = new Array();

    var inputElements = document.getElementsByName(boxId + '-issue[]');
    for(var j = 0; inputElements[j]; ++j){
      if(inputElements[j].checked){
        issueKeysForTest.push(inputElements[j].value);
      }
    }
    issueKeys.push(issueKeysForTest);
  }

  ajaxJirafa.linkIssuesToAll(tests, issueKeys, function (t) {
    location.reload(true);
  });
}

function deleteLinksFromAll() {
  var linksToDelete = new Array();
  var inputElements = document.getElementsByClassName('linkDeleteCheckbox');

  for(var i = 0; inputElements[i]; ++i){
    if(inputElements[i].checked){
    linksToDelete.push(inputElements[i].value);
    }
  }

  ajaxJirafa.deleteLinks(linksToDelete, function (t) {
    location.reload(true);
  });
}

function showAllBoxes(parentBox) {
  var parentElement = document.getElementById(parentBox);

  var showLink = document.getElementById(parentBox + '-show-all');
  var hideLink = document.getElementById(parentBox + '-hide-all');
  showLink.style.display = 'none';
  hideLink.style.display = 'inline';

  var buttons = parentElement.getElementsByClassName('test-showlink');
  for(var i = 0; buttons[i]; ++i) {
    if (!isHidden(buttons[i])) {
      buttons[i].click();
    }
  }

  return false;
}

function hideAllBoxes(parentBox) {
  var parentElement = document.getElementById(parentBox);

  var showLink = document.getElementById(parentBox + '-show-all');
  var hideLink = document.getElementById(parentBox + '-hide-all');
  showLink.style.display = 'inline';
  hideLink.style.display = 'none';

  var buttons = parentElement.getElementsByClassName('test-hidelink');
  for(var i = 0; buttons[i]; ++i) {
    if (!isHidden(buttons[i])) {
      buttons[i].click();
    }
  }

  return false;
}

function isHidden(el) {
  return (el.offsetParent === null)
}
