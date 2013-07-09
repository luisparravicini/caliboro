var Caliboro = {};

Caliboro.data = [];
Caliboro.basePath = '.';

Caliboro.showingRefs = false;
Caliboro.currentBone = null;
Caliboro.imageNormalSize = null;

Caliboro.showReferenceName = function(node) {
  Caliboro.getImageContainer().find('#refName').remove();

  var container = Caliboro.getReferencesContainer();
  var name = node.data('name');
  var refNode = $('<div id="refName">').text(name);
  refNode.css('position', 'absolute').css('display', 'none');
  container.append(refNode);
  var x = Math.round(parseInt(node.css('left')) - refNode.width() / 2);
  var y = parseInt(node.css('top')) + node.height();
  refNode.css('left', x).css('top', y);
  refNode.fadeIn(100);
};

Caliboro.hideReferenceName = function() {
  Caliboro.getReferencesContainer().find('#refName').fadeOut(800, function() { $(this).remove(); });
};

Caliboro.getReferencesContainer = function() {
  var imageContainer = Caliboro.getImageContainer();
  var container = imageContainer.find('#refs')[0];
  if (container == null) {
    container = $('<div id="refs">');
    imageContainer.append(container);
  }

  return $(container);
};

Caliboro.getImage = function() {
  return Caliboro.getImageContainer().find('img.main')[0];
};

Caliboro.showReferences = function() {
  Caliboro.showingRefs = true;

  var container = Caliboro.getReferencesContainer();
  var img = Caliboro.getImage();
  var w = img.width;
  var h = img.height;
  var nw = img.naturalWidth;
  var nh = img.naturalHeight;

  var points = Caliboro.currentImage.points;
  points.forEach(function(point) {
    var img = new Image();
    var dot = $(img).hide().data('name', point.name).
    addClass('reference').hover(function() {
      Caliboro.showReferenceName($(this));
    }, function() {
      Caliboro.hideReferenceName();
    });
    var x = w * point.x / nw;
    var y = h * point.y / nh;
    dot.css('left', x).css('top', y).css('position', 'absolute');
    img.onload = function() {
      dot.css('left', x - this.width/2).css('top', y - this.height/2).show();
    };
    img.src = 'dot.png';
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

Caliboro.changeZoom = function(zoom) {
  var img = Caliboro.getImage();
  zoom /= 100.0;
  $(img).css('width', Caliboro.imageNormalSize.width * zoom);

  Caliboro.updateReferences();
  Caliboro.updateReferencesContainer();
};

Caliboro.showControls = function() {
  var refs = $('<a href="#">').addClass('updateRefs').click(function() {
    Caliboro.toggleReferences();
    return false;
  });
  var zoom = $('<div id="zoomSlider">');
  Caliboro.getControlsContainer().empty().append(refs).append(zoom);

  $(zoom).slider({
    value:100,
    min: 100,
    max: 300,
    step: 25,
    slide: function(event, ui) { Caliboro.changeZoom(ui.value); }
  });

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
    Caliboro.imageNormalSize = { 'width': this.width, 'height': this.height };
    Caliboro.showControls();
    Caliboro.updateReferences();
    Caliboro.updateReferencesContainer();
  }
  img.src = Caliboro.basePath + '/' + Caliboro.currentImage.imagePath;

  Caliboro.getImageContainer().resizable('destroy');
  Caliboro.setupImageContainer();
};

Caliboro.listImages = function(bone) {
  var images = bone['images'];
  var node = $("<ul class='images'>");
  images.forEach(function(image) {
    var linkNode = $("<a href='#'>").text(image.name).data('image-data', image)
    .click(function() {
      Caliboro.getBonesContainer().find('li').removeClass('selected');
      $(this).addClass('selected');
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

Caliboro.updateReferencesContainer = function() {
  var img = Caliboro.getImage();
  if (img) {
    img = $(img);
    var refs = Caliboro.getReferencesContainer();
    if (refs) {
      var width = parseInt(img.css('width'));
      refs.css('width', width + 'px');
      refs.css('height', img.css('height'));

      var x = Math.round((parseInt(refs.parent().css('width')) - width) / 2);
      if (x < 0) {
        x = 0;
      }
      refs.css('left', x + 'px');
      img.css('left', x + 'px');
    }
  }
};

Caliboro.setupImageContainer = function() {
  var container = Caliboro.getImageContainer();
  container.resizable({ handles: "se", minWidth: 400, minHeight: 400 });

  container.resize(function() {
    Caliboro.updateReferencesContainer();
  });
};

Caliboro.init = function() {
  Caliboro.setupImageContainer();
};

