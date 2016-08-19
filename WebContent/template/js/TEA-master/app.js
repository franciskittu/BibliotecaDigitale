(function() {
  var AnnotationView, DocRouter, Document, Editor, ImageView, app;

  Document = Backbone.Model.extend({
    defaults: {
      label: null,
      code: null,
      text: null,
      annotations: null,
      images: null
    },
    urlRoot: 'http://wafi.iit.cnr.it/webvis/annotarium/api/docs'
  });

  DocRouter = Backbone.Router.extend({
    routes: {
      'docs/:id': 'open_document'
    },
    initialize: function(conf) {
      return this.app = conf.app;
    },
    open_document: function(id) {
      var annotation_view, doc, editor, image_view;
      d3.select('#editor').selectAll('*').remove();
      d3.select('#annotation_view').selectAll('*').remove();
      d3.select('#image_view').selectAll('*').remove();
      doc = new Document({
        id: id
      });
      image_view = new ImageView({
        el: '#image_view',
        model: doc
      });
      editor = new Editor({
        el: '#editor',
        breakdown_grammar: this.app.breakdown_grammar,
        model: doc
      });
      annotation_view = new AnnotationView({
        el: '#annotation_view',
        model: doc
      });
      return doc.fetch();
    }
  });

  AnnotationView = Backbone.D3View.extend({
    namespace: null,
    tagName: 'div',
    initialize: function() {
      this.d3el.attr({
        "class": 'AnnotationView'
      });
      return this.listenTo(this.model, 'change:annotations', this.render);
    },
    render: function() {
      var annotations, annotations_data;
      annotations_data = this.model.get('annotations');
      annotations = this.d3el.selectAll('.annotation').data(annotations_data);
      annotations.enter().append('table').attr({
        "class": 'annotation'
      });
      annotations.html(function(d) {
        var id, rowspan, triples;
        rowspan = Math.max(1, d.triples.length);
        triples = '';
        d.triples.forEach(function(t, i) {
          var object;
          if (i > 0) {
            triples += '</tr><tr>';
          }
          object = t.object_type === 'uri' ? "<a href='" + t.object_uri + "'>" + t.object + "</a>" : '"' + t.object + '"';
          return triples += "<td class='predicate'><a href='" + t.predicate_uri + "'>" + t.predicate + "</a></td><td class='object " + t.object_type + "'>" + object + "</td>";
        });
        id = d.id[0] === '_' ? '' : d.id;
        return "<tr><td class='id' rowspan='" + rowspan + "'>" + id + "</td><td rowspan='" + rowspan + "' class='" + d.type + "'>" + (d.text.replace(/\n/g, '↵')) + "</td>" + triples + "</tr>";
      });
      return annotations.exit().remove();
    }
  });

  Editor = Backbone.D3View.extend({
    namespace: null,
    tagName: 'div',
    initialize: function(conf) {
      var bar, dropdown, dropdown_buttons, editor_div, items, predicates, wrapper;
      this.d3el.classed('Editor', true);
      this.save = _.throttle(((function(_this) {
        return function() {
          var o, triples;
          _this.model.save(null, {
            success: function() {
              return _this.save_feedback_icon.classed('hidden', true);
            },
            error: function() {
              return _this.save_feedback_icon.classed('hidden', false);
            }
          });
          triples = [];
          _this.model.attributes.annotations.forEach(function(s) {
            return triples = triples.concat(s.triples.map(function(t) {
              return {
                subject: t.subject,
                predicate: t.predicate_uri,
                object: t.object_uri,
                start: s.start,
                end: s.end
              };
            }));
          });
          o = {
            id: _this.model.attributes.index_id,
            idDoc: _this.model.attributes.index_id,
            code: _this.model.attributes.code,
            name: _this.model.attributes.label,
            text: _this.model.attributes.text,
            triples: triples
          };
          return d3.json('http://wafi.iit.cnr.it:33065/ClaviusWeb-1.0.3/ClaviusGraph/update').post(JSON.stringify(o), function(error, d) {
            if (error) {
              throw error;
            }
          });
        };
      })(this)), 5000, true);
      bar = this.d3el.append('div').attr({
        "class": 'bar'
      });
      bar.append('button').text('Undo').on('click', (function(_this) {
        return function() {
          _this.editor.execCommand('undo');
          _this.editor.focus();
          return _this.update();
        };
      })(this)).style({
        color: '#555'
      }).attr({
        title: 'Cancel the last change.'
      });
      bar.append('button').text('Redo').on('click', (function(_this) {
        return function() {
          _this.editor.execCommand('redo');
          _this.editor.focus();
          return _this.update();
        };
      })(this)).style({
        color: '#555'
      }).attr({
        title: 'Redo the last change.'
      });
      bar.append('button').text('/* */').on('click', (function(_this) {
        return function() {
          var pos;
          _this.editor.replaceSelection('/* comment */');
          pos = _this.editor.getCursor();
          _this.editor.focus();
          _this.update();
          return _this.editor.setSelection({
            line: pos.line,
            ch: 3
          }, {
            line: pos.line,
            ch: 10
          });
        };
      })(this)).style({
        color: '#449444'
      }).attr({
        title: 'COMMENT\nInsert a new comment block.\nThe content of a comment block is not intepreted as text nor as annotations.'
      });
      bar.append('button').text('---').on('click', (function(_this) {
        return function() {
          var pos;
          _this.editor.replaceSelection('--- id');
          pos = _this.editor.getCursor();
          _this.editor.focus();
          _this.update();
          return _this.editor.setSelection({
            line: pos.line,
            ch: 4
          }, {
            line: pos.line,
            ch: 6
          });
        };
      })(this)).style({
        color: 'rgb(183, 58, 58)'
      }).attr({
        title: 'BREAK\nInsert a symbol marking the beginning of a new section of text (e.g, a page).\nA break can be given an ID to be used in RDF triples, to annotate the corresponding text section.'
      });
      bar.append('button').text('{ }').on('click', (function(_this) {
        return function() {
          var selection, sels;
          selection = _this.editor.getSelection();
          sels = _this.editor.listSelections();
          _this.editor.replaceSelection('{' + selection + '}');
          _this.editor.focus();
          return _this.update();
        };
      })(this)).style({
        color: 'rgb(183, 58, 58)'
      }).attr({
        title: 'AUTHOR\'S ADDITION\nInsert a new author\'s addition, or mark the selected text as an author\'s addition.'
      });
      bar.append('button').text('[ ]').on('click', (function(_this) {
        return function() {
          var selection, sels;
          selection = _this.editor.getSelection();
          sels = _this.editor.listSelections();
          _this.editor.replaceSelection('[' + selection + ']');
          _this.editor.focus();
          return _this.update();
        };
      })(this)).style({
        color: 'rgb(183, 58, 58)'
      }).attr({
        title: 'EDITOR\'S ADDITION\nInsert a new editor\'s addition, or mark the selected text as an editor\'s addition.'
      });
      bar.append('button').text('<< >>').on('click', (function(_this) {
        return function() {
          var end, end_offset, selection, sels, start;
          selection = _this.editor.getSelection();
          sels = _this.editor.listSelections();
          _this.editor.replaceSelection('<id<' + selection + '>id>');
          if (sels[0].anchor.line < sels[0].head.line || sels[0].anchor.line === sels[0].head.line && sels[0].anchor.ch < sels[0].head.ch) {
            start = sels[0].anchor;
            end = sels[0].head;
          } else {
            start = sels[0].head;
            end = sels[0].anchor;
          }
          end_offset = start.line === end.line ? 4 : 0;
          _this.editor.focus();
          _this.update();
          return _this.editor.setSelections([
            {
              anchor: {
                line: start.line,
                ch: start.ch + 1
              },
              head: {
                line: start.line,
                ch: start.ch + 3
              }
            }, {
              anchor: {
                line: end.line,
                ch: end.ch + 1 + end_offset
              },
              head: {
                line: end.line,
                ch: end.ch + 3 + end_offset
              }
            }
          ]);
        };
      })(this)).style({
        color: '#1f77b4'
      }).attr({
        title: 'SPAN\nInsert a new span, or transform the selected text into a span.\nA span can be given an ID to be used in RDF triples, to annotate the corresponding text portion.'
      });
      bar.append('button').text('+++').on('click', (function(_this) {
        return function() {
          var pos;
          _this.editor.replaceSelection('+++\nsubj pred obj\n+++');
          pos = _this.editor.getCursor();
          _this.editor.focus();
          _this.update();
          return _this.editor.setSelection({
            line: pos.line - 1,
            ch: 0
          }, {
            line: pos.line - 1,
            ch: 15
          });
        };
      })(this)).style({
        color: '#555'
      }).attr({
        title: 'RDF TRIPLES BLOCK\nInsert a new block for RDF triples.'
      });
      dropdown = bar.append('span').attr({
        "class": 'dropdown_button'
      });
      dropdown_buttons = dropdown.append('div');
      dropdown_buttons.append('button').text('triple').on('click', (function(_this) {
        return function() {
          var pos;
          _this.editor.replaceSelection("subj pred obj");
          pos = _this.editor.getCursor();
          _this.editor.focus();
          _this.update();
          return _this.editor.setSelection({
            line: pos.line,
            ch: 0
          }, {
            line: pos.line,
            ch: 4
          });
        };
      })(this)).style({
        color: '#555'
      }).attr({
        title: 'RDF TRIPLE\nInsert a new RDF triple.\nUse it within a +++ block.'
      });
      dropdown_buttons.append('button').html('&blacktriangledown;').on('click', (function(_this) {
        return function() {
          if (d3.select('.Editor .dropdown_button .items').style('display') === 'none') {
            return d3.select('.Editor .dropdown_button .items').style('display', 'inline');
          } else {
            return d3.select('.Editor .dropdown_button .items').style('display', 'none');
          }
        };
      })(this)).style({
        color: '#555'
      });
      predicates = {
        is: 'its:taIdentRef',
        type: 'its:taClassRef',
        page: 'foaf:page',
        comment: 'rdfs:comment'
      };
      items = dropdown.append('div').attr({
        "class": 'items'
      }).selectAll('.item').data(Object.keys(predicates));
      items.enter().append('div').attr({
        "class": 'item'
      }).text(function(d) {
        return d;
      }).on('click', (function(_this) {
        return function(d) {
          var pos;
          _this.editor.replaceSelection("subj " + d + " obj");
          pos = _this.editor.getCursor();
          _this.editor.focus();
          _this.update();
          _this.editor.setSelection({
            line: pos.line,
            ch: 0
          }, {
            line: pos.line,
            ch: 4
          });
          return d3.select('.Editor .dropdown_button .items').style('display', 'none');
        };
      })(this));
      bar.append('div').attr({
        "class": 'spacer'
      });
      this.save_feedback_icon = bar.append('div').text("Error saving document!").attr({
        "class": 'save_feedback_icon hidden'
      });
      editor_div = this.d3el.append('div').attr({
        "class": 'editor_div'
      }).style({
        position: 'relative'
      });
      wrapper = editor_div.append('div').style({
        position: 'absolute',
        height: '100%',
        width: '100%'
      });
      this.status_bar = this.d3el.append('div').attr({
        "class": 'status_bar'
      });
      this.parser = PEG.buildParser(conf.breakdown_grammar);
      CodeMirror.defineSimpleMode('hl', {
        start: [
          {
            regex: new RegExp('<[a-zA-Z0-9][_a-zA-Z0-9]*<'),
            token: 'span_open'
          }, {
            regex: new RegExp('<<'),
            token: 'span_open'
          }, {
            regex: new RegExp('>[a-zA-Z0-9][_a-zA-Z0-9]*>'),
            token: 'span_close'
          }, {
            regex: new RegExp('>>'),
            token: 'span_close'
          }, {
            regex: new RegExp('^\\+\\+\\+$'),
            token: 'triple_section_open',
            next: 'triple_section'
          }, {
            regex: new RegExp('\\[\.\.\.\\]'),
            token: 'gap'
          }, {
            regex: new RegExp('\\[…\\]'),
            token: 'gap'
          }, {
            regex: new RegExp('\\['),
            token: 'editor_addition_delimiter'
          }, {
            regex: new RegExp('\\]'),
            token: 'editor_addition_delimiter'
          }, {
            regex: new RegExp('\\{'),
            token: 'author_addition_delimiter'
          }, {
            regex: new RegExp('\\}'),
            token: 'author_addition_delimiter'
          }, {
            regex: new RegExp('^(---)(.*)'),
            token: ['break', 'break_id']
          }
        ],
        triple_section: [
          {
            regex: new RegExp('^\\+\\+\\+$'),
            token: 'triple_section_close',
            next: 'start'
          }, {
            regex: new RegExp('(^[^ \t]*)([ \t]+)([^ \t]*)([ \t]+)(".*"$)'),
            token: ['subject', '', 'predicate', '', 'literal']
          }, {
            regex: new RegExp('(^[^ \t]*)([ \t]+)([^ \t]*)([ \t]+)([^" \t]*$)'),
            token: ['subject', '', 'predicate', '', 'object']
          }
        ]
      });
      this.editor = CodeMirror(wrapper.node(), {
        lineWrapping: true
      });
      this.editor.on('keyup', _.throttle(((function(_this) {
        return function() {
          return _this.update();
        };
      })(this)), 200, true));
      this.editor.on('drop', (function(_this) {
        return function() {
          return _this.update();
        };
      })(this));
      return this.listenTo(this.model, 'sync', (function(_this) {
        return function() {
          if (_this.model.previous('code') === null) {
            _this.editor.setValue(_this.model.attributes.code);
            return _this.update();
          }
        };
      })(this));
    },
    update: function() {
      this.compile();
      this.model.set('code', this.editor.getValue());
      return this.save();
    },
    render: function() {
      return this.editor.refresh();
    },
    compile: function() {
      var data, e, error1;
      this.status_bar.text('All ok.');
      this.status_bar.classed('error', false);
      this.editor.getAllMarks().forEach(function(mark) {
        return mark.clear();
      });
      this.section_highlight();
      try {
        data = this.parser.parse(this.editor.getValue());
        this.spans_highlight(data.spans);
        return this.model.set({
          annotations: data.spans,
          text: data.plain_text
        });
      } catch (error1) {
        e = error1;
        this.status_bar.text("Line " + e.location.start.line + ": " + e.message);
        return this.status_bar.classed('error', true);
      }
    },
    spans_highlight: function(spans) {
      return spans.forEach((function(_this) {
        return function(s) {
          return _this.editor.markText({
            line: s.start_code_location.start.line - 1,
            ch: s.start_code_location.start.column - 1
          }, {
            line: s.end_code_location.end.line - 1,
            ch: s.end_code_location.end.column - 1
          }, {
            className: 'span'
          });
        };
      })(this));
    },
    section_highlight: function() {
      var line_number, section_type;
      section_type = null;
      line_number = 0;
      return this.editor.eachLine((function(_this) {
        return function(l) {
          _this.editor.removeLineClass(line_number, 'background');
          _this.editor.removeLineClass(line_number, 'text');
          if (section_type != null) {
            _this.editor.addLineClass(line_number, 'background', section_type + "_section");
            _this.editor.addLineClass(line_number, 'text', section_type + "_section_text");
          }
          if (l.text === '+++' && section_type === 'triples') {
            _this.editor.addLineClass(line_number, 'background', section_type + "_section_close");
            section_type = null;
          } else if (l.text === '+++' && (section_type == null)) {
            section_type = 'triples';
            _this.editor.addLineClass(line_number, 'background', section_type + "_section_open");
            _this.editor.addLineClass(line_number, 'text', section_type + "_section_text");
          }
          if (l.text.slice(0, 2) === '/*' && (section_type == null)) {
            section_type = 'comments';
            _this.editor.addLineClass(line_number, 'background', section_type + "_section_open");
            _this.editor.addLineClass(line_number, 'text', section_type + "_section_text");
          }
          if (l.text.slice(l.text.length - 2, l.text.length) === '*/' && section_type === 'comments') {
            _this.editor.addLineClass(line_number, 'background', section_type + "_section_close");
            section_type = null;
          }
          if (l.text.slice(0, 3) === '---' && (section_type == null)) {
            _this.editor.addLineClass(line_number, 'background', "break_section");
            _this.editor.addLineClass(line_number, 'text', "break_section_text");
          }
          line_number++;
          return false;
        };
      })(this));
    }
  });

  ImageView = Backbone.D3View.extend({
    namespace: null,
    tagName: 'div',
    initialize: function() {
      var bar, svg, zoom, zoomable_layer;
      this.d3el.attr({
        "class": 'ImageView'
      });
      this.listenTo(this.model, 'change:images', this.render);
      bar = this.d3el.append('div').attr({
        "class": 'bar'
      });
      bar.append('button').text('Previous').on('click', (function(_this) {
        return function() {
          if (_this.selected_image !== 0) {
            return _this.render(_this.selected_image - 1);
          }
        };
      })(this)).style({
        color: '#555'
      }).attr({
        title: 'Get the previous image.'
      });
      bar.append('div').attr({
        "class": 'spacer'
      });
      bar.append('button').text('Next').on('click', (function(_this) {
        return function() {
          if (_this.selected_image < _this.model.attributes.images.length - 1) {
            return _this.render(_this.selected_image + 1);
          }
        };
      })(this)).style({
        color: '#555'
      }).attr({
        title: 'Get the next image.'
      });
      svg = this.d3el.append('svg');
      zoomable_layer = svg.append('g').attr({
        "class": 'zoomable'
      });
      zoom = d3.behavior.zoom().scaleExtent([1, Infinity]).on('zoom', function() {
        return zoomable_layer.attr({
          transform: "translate(" + (zoom.translate()) + ")scale(" + (zoom.scale()) + ")"
        });
      });
      return svg.call(zoom);
    },
    render: function(image_index) {
      var data, doc_id, images, images_data;
      images_data = this.model.get('images');
      doc_id = this.model.get('id');
      this.selected_image = typeof image_index === 'object' ? 0 : image_index;
      data = images_data.length === 0 ? [] : [images_data[this.selected_image]];
      images = this.d3el.select('.zoomable').selectAll('image').data(data);
      images.enter().append('image');
      images.attr({
        'xlink:href': function(d) {
          return "/webvis/annotarium/data/images/" + doc_id + "/" + d.id + ".jpg";
        }
      });
      return images.exit().remove();
    }
  });

  app = {};

  d3.text('lib/breakdown.peg.js', function(error, breakdown_grammar) {
    var doc_router;
    app.breakdown_grammar = breakdown_grammar;
    doc_router = new DocRouter({
      app: app
    });
    return Backbone.history.start();
  });

}).call(this);

//# sourceMappingURL=app.js.map
