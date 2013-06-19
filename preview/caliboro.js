var Caliboro = {};

Caliboro.data = [];
Caliboro.basePath = '.';

Caliboro.showingRefs = false;
Caliboro.currentBone = null;

Caliboro.showReferenceName = function(node) {
  Caliboro.getImageContainer().find('#refName').remove();

  var container = Caliboro.getImageContainer();
  var name = node.data('name');
  var refNode = $('<div id="refName">').text(name);
  refNode.css('position', 'absolute').css('display', 'none');
  container.append(refNode);
  var x = parseInt(node.css('left')) - refNode.width() / 2;
  var y = parseInt(node.css('top')) + node.height();
  refNode.css('left', x).css('top', y);
  refNode.fadeIn(100);
};

Caliboro.hideReferenceName = function() {
  Caliboro.getImageContainer().find('#refName').fadeOut(800, function() { $(this).remove(); });
};

Caliboro.showReferences = function() {
  Caliboro.showingRefs = true;

  var container = Caliboro.getImageContainer();
  var img = container.find('img.main')[0];
  var w = img.width;
  var h = img.height;
  var nw = img.naturalWidth;
  var nh = img.naturalHeight;


  var points = Caliboro.currentImage.points;
  points.forEach(function(point) {
    var dot = $('<img src="dot.png">').data('name', point.name).
    addClass('reference').hover(function() {
      Caliboro.showReferenceName($(this));
    }, function() {
      Caliboro.hideReferenceName();
    });
    var x = w * point.x / nw;
    var y = h * point.y / nh;
    dot.css('left', x).css('top', y).css('position', 'absolute');
    container.append(dot);
  });
};

Caliboro.removeReferences = function() {
  Caliboro.getImageContainer().find('img.reference').remove();
  Caliboro.showingRefs = false;
};

Caliboro.updateReferencesButton = function() {
  var btnShowRefs = $(Caliboro.getControlsContainer().find('.updateRefs')[0]);
  if (Caliboro.showingRefs)
    btnShowRefs.empty().append('Ocultar referencias');
  else
    btnShowRefs.empty().append('Ver referencias');
};

Caliboro.toggleReferences = function() {
  if (Caliboro.showingRefs)
    Caliboro.removeReferences();
  else
    Caliboro.showReferences();

  Caliboro.updateReferencesButton();
};

Caliboro.updateReferences = function() {
  if (Caliboro.showingRefs) {
    Caliboro.removeReferences();
    Caliboro.showReferences();
  }
};

Caliboro.getControlsContainer = function() {
  return $('#controls');
};

Caliboro.showControls = function() {
  var refs = $('<a href="#">').addClass('updateRefs').click(function() {
    Caliboro.toggleReferences();
    return false;
  });
  Caliboro.getControlsContainer().empty().append(refs);
  Caliboro.updateReferencesButton();
};

Caliboro.getImageContainer = function() {
  return $('#image');
};

Caliboro.showImage = function(node) {
  Caliboro.currentImage = $(node).data('image-data');

  var img = new Image();
  $(img).addClass('main');
  Caliboro.getImageContainer().empty().append(img);
  img.onload = function() {
    Caliboro.showControls();
    Caliboro.updateReferences();
  }
  img.src = Caliboro.basePath + '/' + Caliboro.currentImage.imagePath;
};

Caliboro.listImages = function(bone) {
  var images = bone['images'];
  var node = $("<ul class='images'>");
  images.forEach(function(image) {
    var linkNode = $("<a href='#'>").text(image.name).data('image-data', image)
    .click(function() {
      Caliboro.getBonesContainer().find('li').removeClass('selected');
      $(this).parent().addClass('selected');
      Caliboro.showImage(this);
      return false;
    });
    node.append($("<li>").append(linkNode));
  });

  return node;
};

Caliboro.getBonesContainer = function() {
  return $('#bones');
};

Caliboro.listBones = function() {
  var div = Caliboro.getBonesContainer();
  div.empty();

  var bones = Caliboro.data;
  var node = $("<ul class='bones'>")
  bones.forEach(function(bone) {
    node.append($("<li>").text(bone.name).append(Caliboro.listImages(bone)));
  });

  div.append(node);
};
