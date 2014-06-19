// author: Samuel Mueller 
// homepage: http://github.com/ssmm/angular-table 
// version: 0.0.2 
(function() {
  angular.module("angular-table", []);

  angular.module("angular-table").service("attributeExtractor", function() {
    return {
      extractWidth: function(classes) {
        var width;

        width = /([0-9]+px)/i.exec(classes);
        if (width) {
          return width[0];
        } else {
          return "";
        }
      },
      isSortable: function(classes) {
        var sortable;

        sortable = /(sortable)/i.exec(classes);
        if (sortable) {
          return true;
        } else {
          return false;
        }
      },
      extractTitle: function(td) {
        return td.attr("title") || td.attr("attribute");
      },
      extractAttribute: function(td) {
        return td.attr("attribute");
      }
    };
  });

  angular.module("angular-table").directive("atTable", [
    "attributeExtractor", function(attributeExtractor) {
      var PaginationSetup, StandardSetup, capitaliseFirstLetter, constructHeader, createSetup, setupTr, validateInput;

      capitaliseFirstLetter = function(string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
      };
      constructHeader = function(element) {
        var attribute, existing_ths, icon, sortable, td, tds, th, thead, title, tr, width, _i, _j, _len, _len1, _ref;

        thead = element.find("thead");
        if (thead[0]) {
          tr = thead.find("tr");
          existing_ths = {};
          _ref = tr.find("th");
          for (_i = 0, _len = _ref.length; _i < _len; _i++) {
            th = _ref[_i];
            th = angular.element(th);
            existing_ths[th.attr("attribute")] = th.html();
          }
          tr.remove();
          tr = $("<tr></tr>");
          tds = element.find("td");
          for (_j = 0, _len1 = tds.length; _j < _len1; _j++) {
            td = tds[_j];
            td = angular.element(td);
            attribute = attributeExtractor.extractAttribute(td);
            th = $("<th style='cursor: pointer; -webkit-user-select: none;'></th>");
            title = existing_ths[attribute] || capitaliseFirstLetter(attributeExtractor.extractTitle(td));
            th.html("" + title);
            sortable = td[0].attributes.sortable || attributeExtractor.isSortable(td.attr("class"));
            if (sortable) {
              th.attr("ng-click", "predicate = '" + attribute + "'; descending = !descending;");
              icon = angular.element("<i style='margin-left: 10px;'></i>");
              icon.attr("ng-class", "getSortIcon('" + attribute + "')");
              th.append(icon);
            }
            width = attributeExtractor.extractWidth(td.attr("class"));
            th.attr("width", width);
            tr.append(th);
          }
          return thead.append(tr);
        }
      };
      validateInput = function(attributes) {
        if (attributes.pagination && attributes.list) {
          throw "You can not specify a list if you have specified a pagination. The list defined for the pagnination will automatically be used.";
        }
        if (!attributes.pagination && !attributes.list) {
          throw "Either a list or pagination must be specified.";
        }
      };
      setupTr = function(element, repeatString) {
        var tbody, tr;

        tbody = element.find("tbody");
        tr = tbody.find("tr");
        tr.attr("ng-repeat", repeatString);
        return tbody;
      };
      StandardSetup = function(attributes) {
        this.repeatString = "item in " + attributes.list + " | orderBy:predicate:descending";
        this.compile = function(element, attributes, transclude) {
          return setupTr(element, this.repeatString);
        };
        this.link = function() {};
      };
      PaginationSetup = function(attributes) {
        this.repeatString = "item in " + attributes.pagination + ".list | limitTo:fromPage() | limitTo:toPage() | orderBy:predicate:descending";
        this.compile = function(element, attributes, transclude) {
          var fillerTr, tbody, td, tdString, tds, _i, _len;

          tbody = setupTr(element, this.repeatString);
          if (typeof attributes.fillLastPage !== "undefined") {
            tds = element.find("td");
            tdString = "";
            for (_i = 0, _len = tds.length; _i < _len; _i++) {
              td = tds[_i];
              tdString += "<td>{{item}}&nbsp;</td>";
            }
            fillerTr = angular.element("<tr>" + tdString + "</tr>");
            fillerTr.attr("ng-repeat", "item in " + attributes.pagination + ".getFillerArray() ");
            return tbody.append(fillerTr);
          }
        };
        this.link = function($scope, $element, $attributes) {
          var paginationName;

          paginationName = attributes.pagination;
          $scope.fromPage = function() {
            if ($scope[paginationName]) {
              return $scope[paginationName].fromPage();
            }
          };
          return $scope.toPage = function() {
            if ($scope[paginationName]) {
              return $scope[paginationName].itemsPerPage;
            }
          };
        };
      };
      createSetup = function(attributes) {
        validateInput(attributes);
        if (attributes.list) {
          return new StandardSetup(attributes);
        }
        if (attributes.pagination) {
          return new PaginationSetup(attributes);
        }
      };
      return {
        restrict: "AC",
        scope: true,
        compile: function(element, attributes, transclude) {
          var setup;

          setup = createSetup(attributes);
          constructHeader(element);
          setup.compile(element, attributes, transclude);
          return {
            post: function($scope, $element, $attributes) {
              $scope.getSortIcon = function(predicate) {
                if (predicate !== $scope.predicate) {
                  return "icon-minus";
                }
                if ($scope.descending) {
                  return "icon-chevron-down";
                } else {
                  return "icon-chevron-up";
                }
              };
              return setup.link($scope, $element, $attributes);
            }
          };
        }
      };
    }
  ]);

  angular.module("angular-table").directive("atImplicit", [
    "attributeExtractor", function(attributeExtractor) {
      return {
        restrict: "AC",
        compile: function(element, attributes, transclude) {
          var attribute;

          attribute = attributeExtractor.extractAttribute(element);
          return element.append("{{item." + attribute + "}}");
        }
      };
    }
  ]);

  angular.module("angular-table").directive("atPagination", [
    "attributeExtractor", function(attributeExtractor) {
      return {
        replace: true,
        restrict: "E",
        template: "      <div class='pagination' style='margin: 0px;'>        <ul>          <li ng-class='{disabled: currentPage <= 0}'>            <a href='' ng-click='goToPage(currentPage - 1)'>&laquo;</a>          </li>          <li ng-class='{active: currentPage == page}' ng-repeat='page in pages'>            <a href='' ng-click='goToPage(page)'>{{page + 1}}</a>          </li>          <li ng-class='{disabled: currentPage >= numberOfPages - 1}'>            <a href='' ng-click='goToPage(currentPage + 1); normalize()'>&raquo;</a>          </li>        </ul>      </div>",
        scope: {
          itemsPerPage: "@",
          instance: "=",
          list: "="
        },
        link: function($scope, $element, $attributes) {
          $scope.instance = $scope;
          $scope.currentPage = 0;
          $scope.update = function() {
            var x;

            $scope.currentPage = 0;
            if ($scope.list) {
              if ($scope.list.length > 0) {
                $scope.numberOfPages = Math.ceil($scope.list.length / $scope.itemsPerPage);
                $scope.pages = (function() {
                  var _i, _ref, _results;

                  _results = [];
                  for (x = _i = 0, _ref = $scope.numberOfPages - 1; 0 <= _ref ? _i <= _ref : _i >= _ref; x = 0 <= _ref ? ++_i : --_i) {
                    _results.push(x);
                  }
                  return _results;
                })();
              } else {
                $scope.numberOfPages = 0;
                $scope.pages = [];
              }
              return $scope.list = $scope.list;
            }
          };
          $scope.fromPage = function() {
            if ($scope.list) {
              return $scope.itemsPerPage * $scope.currentPage - $scope.list.length;
            }
          };
          $scope.getFillerArray = function() {
            var fillerLength, itemCountOnLastPage, x, _i, _ref, _ref1, _results;

            if ($scope.currentPage === $scope.numberOfPages - 1) {
              itemCountOnLastPage = $scope.list.length % $scope.itemsPerPage;
              if (itemCountOnLastPage !== 0) {
                fillerLength = $scope.itemsPerPage - itemCountOnLastPage - 1;
                _results = [];
                for (x = _i = _ref = $scope.list.length, _ref1 = $scope.list.length + fillerLength; _ref <= _ref1 ? _i <= _ref1 : _i >= _ref1; x = _ref <= _ref1 ? ++_i : --_i) {
                  _results.push(x);
                }
                return _results;
              } else {
                return [];
              }
            }
          };
          $scope.goToPage = function(page) {
            page = Math.max(0, page);
            page = Math.min($scope.numberOfPages - 1, page);
            return $scope.currentPage = page;
          };
          $scope.update();
          return $scope.$watch("list", function() {
            return $scope.update();
          });
        }
      };
    }
  ])
  .directive("atServerPagination", [
    "attributeExtractor", function(attributeExtractor) {
      return {
        replace: true,
        restrict: "A",
        template: "      <div class='pagination' style='margin: 0px;'>        <ul>          <li ng-class='{disabled: currentPage <= 0}'>            <a href='' ng-click='goToPage(currentPage - 1)'>&laquo;</a>          </li>          <li ng-class='{active: currentPage == page}' ng-repeat='page in pages'>            <a href='' ng-click='goToPage(page)'>{{page + 1}}</a>          </li>          <li ng-class='{disabled: currentPage >= numberOfPages - 1}'>            <a href='' ng-click='goToPage(currentPage + 1); normalize()'>&raquo;</a>          </li>        </ul>      </div>",
        scope: {
          itemsPerPage: "@",
          instance: "=",
          list: "=",
          paging: "="
        },
        link: function($scope, $element, $attributes) {
          $scope.instance = $scope;
          $scope.currentPage = $scope.paging.curPage - 1;
          $scope.numberOfPages = Math.ceil($scope.paging.total / $scope.itemsPerPage);
          $scope.update = function() {
            var x;

            $scope.currentPage = $scope.paging.curPage - 1;
            if ($scope.list) {
              if ($scope.list.length > 0) {
                $scope.numberOfPages = Math.ceil($scope.paging.total / $scope.itemsPerPage);
                $scope.pages = (function() {
                  var _i, _ref, _results;

                  _results = [];
                  for (x = _i = 0, _ref = $scope.numberOfPages - 1; 0 <= _ref ? _i <= _ref : _i >= _ref; x = 0 <= _ref ? ++_i : --_i) {
                    _results.push(x);
                  }
                  return _results;
                })();
              } else {
                $scope.numberOfPages = 0;
                $scope.pages = [];
              }
              return $scope.list = $scope.list;
            }
          };
          $scope.fromPage = function() {
              return $scope.itemsPerPage;
          };
          $scope.getFillerArray = function() {
            return $scope.list;
          };
          $scope.goToPage = function(page) {
            page = Math.max(0, page);
            page = Math.min($scope.numberOfPages - 1, page);
            $scope.paging.goToPage(page + 1, $scope.itemsPerPage);
          }
          $scope.update();
          return $scope.$watch("list", function() {
            return $scope.update();
          });
        }
      };
    }
  ]);

}).call(this);
