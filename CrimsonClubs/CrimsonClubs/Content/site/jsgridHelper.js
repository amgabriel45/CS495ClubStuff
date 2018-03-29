//To be run after main jsGrid file loaded

MyConfigField = function (config) {
    jsGrid.Field.call(this, config);
};
MyConfigField.prototype = new jsGrid.Field({
    align: "center",
    formManager: null,
    viewURL: null,
    saveURL: null,
    deleteURL: null,
    hideDetails: null,

    itemTemplate: function (value, item) {
        var $result = jsGrid.fields.control.prototype.itemTemplate.apply(this, arguments);
        var $pageManager = this.formManager.pageManager;
        var $formManager = this.formManager;
        var $viewURL = this.viewURL;
        var $saveURL = this.saveURL;
        var $deleteURL = this.deleteURL;
        var $hideDetails = this.hideDetails || false;

        var $detailButton = $("<a>")
        .click(function (e) {
            $.ajax({ type: "GET", url: $viewURL + "?id=" + item.Id }).then(
                function (data) {
                    $formManager.putData(data);
                    $pageManager.showView();
                });
            $pageManager.highlightRow($(this).closest("tr"));
            $("#viewText").text(item.Id);
        }).append($("<i>").addClass("md-search").css("fontSize", "24px"))
        .tooltip({ title: "View" })
        .append("&nbsp;")
        .prepend("&nbsp;");

        var $editButton = $("<a>")
        .click(function (e) {
            $.ajax({ type: "GET", url: $saveURL + "?id=" + item.Id }).then(
                function (data) {
                    $formManager.putData(data);
                    $pageManager.showEdit();
                });
            $pageManager.highlightRow($(this).closest("tr"));
            $("#editText").text(item.Id);
        }).append($("<i>").addClass("md-edit").css("fontSize", "24px"))
        .tooltip({ title: "Edit" })
        .append("&nbsp;")
        .prepend("&nbsp;");

        var $deleteButton = $("<a>")
        .click(function (e) {
            $.ajax({ type: "GET", url: $deleteURL + "?id=" + item.Id }).then(
                function (data) {
                    $formManager.putData(data);
                    $pageManager.showDelete();
                });
            $pageManager.highlightRow($(this).closest("tr"));
            $("#deleteText").text(item.Id);
        }).append($("<i>").addClass("md-delete").css("fontSize", "24px"))
        .tooltip({ title: "Delete" })
        .append("&nbsp;")
        .prepend("&nbsp;");

        var $detailsButton = ""
        if (!$hideDetails) {
            $detailsButton = $("<a href='/Projects/Details/" + item.Id + "' style='color:inherit' target='_blank'>")
                .append($("<i>").addClass("md-search-in-file").css("fontSize", "24px"))
            .tooltip({ title: "Details" })
            .append("&nbsp;")
            .prepend("&nbsp;");
        }

        return $result.add($detailButton).add($editButton).add($deleteButton).add($detailsButton);
    },
    filterTemplate: function () {
        this._filterButton = $('<div class="btn btn-sm btn-info" id="applyFilterButton">Filter</div>');
        return $("<div>").append(this._filterButton);
    }
});
jsGrid.fields.mycontrol = MyConfigField;

ModalFormConfigField = function (config) {
    jsGrid.Field.call(this, config);
};
ModalFormConfigField.prototype = new jsGrid.Field({
    align: "center",
    formManager: null,
    viewURL: null,

    itemTemplate: function (value, item) {
        var $result = jsGrid.fields.control.prototype.itemTemplate.apply(this, arguments);
        var $pageManager = this.formManager.pageManager;
        var $formManager = this.formManager;
        var $viewURL = this.viewURL;
        var $showDetails = this.showDetails || false;
        var $detailsURL = this.detailsURL;

        var $viewButton = $("<a>")
        .click(function (e) {
            $.ajax({ type: "GET", url: $viewURL + "?id=" + item.Id }).then(
                function (data) {
                    $formManager.putData(data);
                    $pageManager.showView();
                });
            $pageManager.highlightRow($(this).closest("tr"));
        }).append($("<i>").addClass("md-search").css("fontSize", "24px"))
        .tooltip({ title: "View" });

        var $detailsButton = ""
        if ($showDetails) {
            $detailsButton = $("<a href='" + $detailsURL + item.Id + "' style='color:inherit' >")
                .append($("<i>").addClass("md-search-in-file").css("fontSize", "24px"))
            .tooltip({ title: "Details" })
            .append("&nbsp;")
            .prepend("&nbsp;");
        }

        return $result.add($viewButton).add($detailsButton);
    },
    filterTemplate: function () {
        this._filterButton = $('<div class="btn btn-sm btn-info" id="applyFilterButton">Filter</div>');
        return $("<div>").append(this._filterButton);
    }
});
jsGrid.fields.modalformconfig = ModalFormConfigField;

CreateFormConfigField = function (config) {
    jsGrid.Field.call(this, config);
}
CreateFormConfigField.prototype = new jsGrid.Field({
    align: "Center",
    formManager: null,
    populateFields: null,
    // populateFields: [ { name: <field name in item>, selector: <hidden field to update> }, ... ]

    itemTemplate: function (value, item) {
        var $result = jsGrid.fields.control.prototype.itemTemplate.apply(this, arguments);
        var $pageManager = this.formManager.pageManager;
        var $formManager = this.formManager;
        var $fields = this.populateFields;

        var $createButton = $("<a>")
        .click(function (e) {
            $formManager.createFunction();
            $.each($fields, function (index, field) {
                try {
                    $(field.selector).val(item[field.name]);
                } catch (e) {}
                try {
                    $(field.selector).text(item[field.name]);
                } catch (e) { }
            });
        }).append($("<i>").addClass("md-plus").css("fontSize", "24px"))
        .tooltip({ title: "Create New" });

        return $result.add($createButton);
    }
});
jsGrid.fields.createformconfig = CreateFormConfigField;

ImageField = function (config) {
    jsGrid.Field.call(this, config);
};
ImageField.prototype = new jsGrid.Field({
    align: "center",

    itemTemplate: function (value, item) {
        var $result = jsGrid.fields.control.prototype.itemTemplate.apply(this, arguments);

        var url = "/Content/images/gray.png";
        if (value == 1)
            url = "/Content/images/green.png";
        else if (value == 2)
            url = "/Content/images/yellow.png";
        else if (value == 3)
            url = "/Content/images/red.png";

        var $image = $('<img src="' + url + '" style="height:30px;" />');

        return $result.add($image);
    },
    filterTemplate: function () {
        this._statusPicker = $('<select>\
                        <option value=0></option>\
                        <option value=1>Green</option>\
                        <option value=2>Yellow</option>\
                        <option value=3>Red</option>\
                        </select>');
        return $("<div>").append(this._statusPicker);
    },
    filterValue: function () {
        return this._statusPicker.val();
    }
});
jsGrid.fields.imagefield = ImageField;

MoneyField = function (config) {
    jsGrid.Field.call(this, config);
};
MoneyField.prototype = new jsGrid.Field({
    align: "right",
    itemTemplate: function (value, item) {
        var $result = jsGrid.fields.control.prototype.itemTemplate.apply(this, arguments);

        this._value = value;
        var money = '$' + value.toFixed(2).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");
        return $result.add($('<span>' + money + '</span>'));
    },
    sorter: function (money1, money2) {
        return money1 - money2;
    },
    insertTemplate: function (value) {
        return this._insertMoney = $('<div class="input-group input-group-icon"><span class="input-group-addon"><span class="icon md-money"></span></span><input type="text" class="form-control" /></div>');
    },
    editTemplate: function (value) {
        return this._editMoney = $('<div class="input-group input-group-icon"><span class="input-group-addon"><span class="icon md-money"></span></span><input type="text" class="form-control" value="' + value + '"/></div>');
    },
    insertValue: function () {
        var val = parseFloat(this._insertMoney.find("input").val());
        if (isNaN(val))
            val = 0;
        return val;
    },
    editValue: function () {
        var val = parseFloat(this._editMoney.find("input").val());
        if (isNaN(val))
            val = 0;
        return val;
    }
})
jsGrid.fields.moneyfield = MoneyField;

DateField = function (config) {
    jsGrid.Field.call(this, config);
};
DateField.prototype = new jsGrid.Field({
    sorter: function (date1, date2) {
        return Date.parse(date1) - Date.parse(date2);
    }
})
jsGrid.fields.datefield = DateField;

MonthField = function (config) {
    jsGrid.Field.call(this, config);
}
MonthField.prototype = new jsGrid.Field({
    align: "center",

    sorter: function (date1, date2) {
        return Date.parse(date1) - Date.parse(date2);
    },
    itemTemplate: function (value) {
        var date = new Date(value);
        //return (date.getMonth()+1) + "/" + date.getFullYear();
        return date.toLocaleString("en-us", { year: "numeric", month: "long" });
    },
    insertTemplate: function (value) {
        this.insertDate = $("<div><input></div>");
        this.insertDate.find("input").datepicker({ format: "MM yyyy", viewMode: "months", minViewMode: "months", orientation: "bottom" });
        return this.insertDate;
    },
    editTemplate: function (value) {
        this.editDate = $("<div><input></div>");
        this.editDate.find("input").datepicker({ format: "MM yyyy", viewMode: "months", minViewMode: "months", orientation: "bottom" }).datepicker("update", new Date(value));
        return this.editDate;
    },
    insertValue: function (value) {
        var d = new Date(this.insertDate.find("input").datepicker("getDate"));
        var date = new Date(d.getFullYear(), d.getMonth(), 1);
        return date.toDateString();
    },
    editValue: function (value) {
        var d = new Date(this.editDate.find("input").datepicker("getDate"));
        var date = new Date(d.getFullYear(), d.getMonth(), 1);
        return date.toDateString();
    }
});
jsGrid.fields.monthfield = MonthField;

PercentField = function (config) {
    jsGrid.Field.call(this, config);
};
PercentField.prototype = new jsGrid.Field({
    align: "center",

    itemTemplate: function (value, item) {
        var $result = jsGrid.fields.control.prototype.itemTemplate.apply(this, arguments);

        var progBarColor = ""
        if (item.StatusId == 1)
            progBarColor = "progress-bar-success";
        else if (item.StatusId == 2)
            progBarColor = "progress-bar-warning";
        else if (item.StatusId == 3)
            progBarColor = "progress-bar-danger";

        var $progress = $('<div class="progress smallprogress">\
                            <div class="progress-bar ' + progBarColor + '" role="progressbar" style="width:' + value + '%">' + value + '%</div>\
                            </div>');

        return $result.add($progress);
    }
});
jsGrid.fields.percentfield = PercentField;

DropdownField = function (config) {
    jsGrid.Field.call(this, config);
}
DropdownField.prototype = new jsGrid.Field({
    align: "center",

    selectOptions: function (dataSource) {
        var options = { data: dataSource };
        if (!this.searchableItems)
            options = $.extend({ "minimumResultsForSearch": "Infinity" }, options);
        return options;
    },
    itemTemplate: function (value) {
        for (var i = 0; i < this.selectableItems.length; i++) {
            if (this.selectableItems[i].id == value)
                return this.selectableItems[i].text;
        }
        return "";
    },
    insertTemplate: function (value) {
        if (!this.readOnly) {
            this.insertPicker = $('<div><select></div>');
            this.insertPicker.find('select').select2(this.selectOptions(this.selectableItems));
            this.insertPicker.find('.select2').css("width", "auto");
            return this.insertPicker;
        } else {
            return "";
        }
    },
    editTemplate: function (value) {
        if (!this.readOnly) {
            this.editPicker = $('<div><select></div>');
            this.editPicker.find('select').select2(this.selectOptions(this.selectableItems)).val(value);
            this.editPicker.find('.select2').css("width", "auto");
            return this.editPicker;
        } else {
            return "";
        }
    },
    insertValue: function () {
        if (!this.readOnly)
            return this.insertPicker.find('select').val();
        else
            return "";
    },
    editValue: function () {
        if (!this.readOnly)
            return this.editPicker.find('select').val();
        else
            return "";
    },
    filterTemplate: function () {
        this.filterPicker = $('<div><select></div>');
        this.filterPicker.find('select').select2(this.selectOptions(this.filterableItems));
        this.filterPicker.find('.select2').css("width", "auto");
        return this.filterPicker;
    },
    filterValue: function () {
        var value = this.filterPicker.find('select').val();
        return value;
    },
    sorter: function (val1, val2) {
        return val1 - val2;
    }
});
jsGrid.fields.dropdownfield = DropdownField;

SplitField = function (config) {
    jsGrid.Field.call(this, config);
}
SplitField.prototype = new jsGrid.Field({
    itemTemplate: function (value, item) {
        value.sort(function (a, b) {
            return a.id - b.id;
        });

        var $result = $('<table class="inner-table">');
        for (var i = 0; i < value.length; i++) {
            var row = $('<tr class="inner-tr"><td class="inner-td"></td></tr>');
            if (i % 2 == 0)
                row.addClass('inner-alt-row');
            if (this.subtype && Object.keys(jsGrid.fields).indexOf(this.subtype) != -1) {
                var subtype = jsGrid.fields[this.subtype];
                subtype(this);
                subtype.prototype = Object.assign(subtype.prototype, this.subtypeprops);
                row.find('td').append(subtype.prototype.itemTemplate(value[i][this.subname], value[i]));
            }
            $result.append(row);
        }

        return $result;
    }
});
jsGrid.fields.splitfield = SplitField;