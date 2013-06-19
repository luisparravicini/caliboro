var Huesitos = {};

Huesitos.data = [];
Huesitos.basePath = '.';

Huesitos.showingRefs = false;
Huesitos.currentBone = null;

Huesitos.showReferences = function() {
  Huesitos.showingRefs = true;

  var container = Huesitos.getImageContainer();
  var img = container.find('img.main')[0];
  var w = img.width;
  var h = img.height;
  var nw = img.naturalWidth;
  var nh = img.naturalHeight;


  var points = Huesitos.currentImage.points;
  points.forEach(function(point) {
    var dot = $('<img src="dot.png">').addClass('reference');
    var x = w * point.x / nw;
    var y = h * point.y / nh;
    dot.css('left', x).css('top', y).css('position', 'absolute');
    container.append(dot);
  });
};

Huesitos.removeReferences = function() {
  Huesitos.getImageContainer().find('img.reference').remove();
  Huesitos.showingRefs = false;
};

Huesitos.updateReferencesButton = function() {
  var btnShowRefs = $(Huesitos.getControlsContainer().find('.updateRefs')[0]);
  if (Huesitos.showingRefs)
    btnShowRefs.empty().append('Ocultar referencias');
  else
    btnShowRefs.empty().append('Ver referencias');
};

Huesitos.toggleReferences = function() {
  if (Huesitos.showingRefs)
    Huesitos.removeReferences();
  else
    Huesitos.showReferences();

  Huesitos.updateReferencesButton();
};

Huesitos.updateReferences = function() {
  if (Huesitos.showingRefs) {
    Huesitos.removeReferences();
    Huesitos.showReferences();
  }
};

Huesitos.getControlsContainer = function() {
  return $('#controls');
};

Huesitos.showControls = function() {
  var refs = $('<a href="#">').addClass('updateRefs').click(function() {
    Huesitos.toggleReferences();
    return false;
  });
  Huesitos.getControlsContainer().empty().append(refs);
  Huesitos.updateReferencesButton();
};

Huesitos.getImageContainer = function() {
  return $('#image');
};

Huesitos.showImage = function(node) {
  Huesitos.currentImage = $(node).data('image-data');

  var img = new Image();
  $(img).addClass('main');
  Huesitos.getImageContainer().empty().append(img);
  img.onload = function() {
    Huesitos.showControls();
    Huesitos.updateReferences();
  }
  img.src = Huesitos.basePath + '/' + Huesitos.currentImage.imagePath;
};

Huesitos.listImages = function(bone) {
  var images = bone['images'];
  var node = $("<ul class='images'>");
  images.forEach(function(image) {
    var linkNode = $("<a href='#'>").append(image.name).data('image-data', image)
    .click(function() {
      Huesitos.getBonesContainer().find('li').removeClass('selected');
      $(this).parent().addClass('selected');
      Huesitos.showImage(this);
      return false;
    });
    node.append($("<li>").append(linkNode));
  });

  return node;
};

Huesitos.getBonesContainer = function() {
  return $('#bones');
};

Huesitos.listBones = function() {
  var div = Huesitos.getBonesContainer();
  div.empty();

  var bones = Huesitos.data['bones'];
  var node = $("<ul class='bones'>")
  bones.forEach(function(bone) {
    node.append($("<li>").append(bone.name).append(Huesitos.listImages(bone)));
  });

  div.append(node);
};
