var CFSRemark = function (container) {

    container.PageManager = function (name, selector, options) {
        var manager = {};
        manager.name = name;
        manager.selector = selector;
        manager.options = options;

        manager.highlightRow = function (row) {
            $('tr.highlight').removeClass('highlight');
            if (row)
                row.addClass('highlight');
        }
        manager.removeRowHighlight = function () {
            $('tr.highlight').removeClass('highlight');
        }
        manager.showView = function (title) {
            manager.show(title || "View " + manager.name);
            $(manager.selector).find('input, select, .md-delete, .edit-only').prop('disabled', true);
            $(manager.selector).find('.saveButton, .deleteButton, .cancelButton').hide();
            $(manager.selector).find('.closeButton, .editButton, .delButton').show();
            $(manager.selector).trigger('CFS.page.showView');
        }
        manager.showEdit = function (title) {
            manager.show(title || "Edit " + manager.name);
            $(manager.selector).find('input, select, .md-delete, .edit-only').prop('disabled', false);
            $(manager.selector).find('.saveButton, .cancelButton').show();
            $(manager.selector).find('.deleteButton, .closeButton, .editButton, .delButton').hide();
            $(manager.selector).trigger('CFS.page.showEdit');
        }
        manager.showDelete = function (title) {
            manager.show(title || "Delete " + manager.name);
            $(manager.selector).find('input, select, .md-delete, .edit-only').prop('disabled', true);
            $(manager.selector).find('.deleteButton, .cancelButton').show();
            $(manager.selector).find('.saveButton, .closeButton, .editButton, .delButton').hide();
            $(manager.selector).trigger('CFS.page.showDelete');
        }

        return manager;
    }

    container.FormFieldType = function (types) {
        types.Input = {
            getValue: function (selector, manager) {
                return $(selector).val();
            },
            clearValue: function (selector, manager) {
                $(selector).addClass('empty').val("");
            },
            setValue: function (value, selector, manager) {
                $(selector).removeClass('empty').val(value);
            }
        };
        types.Select = {
            getValue: function (selector, manager) {
                return $(selector).val();
            },
            clearValue: function (selector, manager) {
                $(selector).val(0).change();
            },
            setValue: function (value, selector, manager) {
                $(selector).val(value).change();
            }
        };
        types.Switch = {
            getValue: function (selector, manager) {
                return $(selector)[0].checked;
            },
            clearValue: function (selector, manager) {
                $(selector).iCheck('uncheck');
            },
            setValue: function (value, selector, manager) {
                $(selector).iCheck((value && value != "N") ? "check" : "uncheck");
            }
        };
        types.Span = {
            getValue: function (selector, manager) {
                return $(selector).text();
            },
            clearValue: function (selector, manager) {
                $(selector).text("");
            },
            setValue: function (value, selector, manager) {
                $(selector).text(value);
            }
        };

        return types;
    }(container.FormFieldType || {});

    container.FormManager = function (selector, fields, options, pageManager) {
        var manager = {};
        manager.selector = selector;
        manager.fields = fields;
        manager.options = options;
        manager.pageManager = pageManager;
        manager.currentObject = null;

        /*
        var options = {
            saveURL,
            deleteURL,
            deleteIdSelector: function (manager),
            ajaxCompleteCallback: function(data, manager)
        }
        */
        /*var field = {
            field: "field-name-in-dom",
            selector: "jquery selector",
            type: CFSRemark.FormFieldType,
            getValue: func(selector, manager),
            clearValue: func(selector, manager),
            setValue: func(value, selector, manager)
        }*/

        manager.putData = function (data) {
            manager.currentObject = data;
            for (var index in fields) {
                var fieldObj = fields[index];
                var field = fieldObj.field;
                if (data[field]) {
                    var setFunc = fieldObj.setValue || fieldObj.type.setValue;
                    setFunc(data[field], fieldObj.selector, manager);
                } else {
                    var clearFunc = fieldObj.clearValue || fieldObj.type.clearValue;
                    clearFunc(fieldObj.selector, manager);
                }
            }
            var errors = data.Errors || {};
            manager.putErrors(errors);
            manager.currentObject = null;
        }
        manager.putErrors = function (errors) {
            manager.clearErrors();
            for (var key in errors) {
                if (errors.hasOwnProperty(key)) {
                    $("[data-field=" + key + "]").closest('.form-group').addClass('has-danger').append("<label class='form-control-label ajax-error'>" + errors[key] + "</label>");
                }
            }
        }

        manager.clearData = function () {
            for (var index in fields) {
                var fieldObj = fields[index];
                var field = fieldObj.field;
                var clearFunc = fieldObj.clearValue || fieldObj.type.clearValue;
                clearFunc(fieldObj.selector, manager);
            }
            manager.clearErrors();
        }
        manager.clearErrors = function () {
            $(".has-danger").removeClass("has-danger")
            $(".ajax-error").remove();
        }

        manager.getData = function () {
            var data = new FormData()
            for (var index in fields) {
                var fieldObj = fields[index];
                var field = fieldObj.field;
                var getFunc = fieldObj.getValue || fieldObj.type.getValue;
                var value = getFunc(fieldObj.selector, manager);
                if (value != null && value.constructor === Array) {
                    objectToFormData(value, data, field);
                } else {
                    data.append(field, value);
                }
            }
            return data;
        }

        manager.ajaxFunction = function (url, data, successMessage, failMessage, successCallback, failCallback, doneCallback) {
            $.ajax({ type: "POST", url: url, data: data, processData: false, contentType: false }).then(
                function (data) {
                    if (data == "True" || data == true || data.hasErrors == false) {
                        if (successCallback)
                            successCallback(data);
                        toastr.options = {
                            "positionClass": "toast-bottom-full-width",
                        }
                        toastr["success"](successMessage, "Success");
                    } else {
                        if (failCallback)
                            failCallback(data);
                        toastr.options = {
                            "positionClass": "toast-bottom-full-width",
                        }
                        failMessage = data.failMessage || failMessage;
                        toastr["error"](failMessage, "Error");
                    }
                    if (doneCallback)
                        doneCallback(data);
                    if (manager.options.ajaxCompleteCallback)
                        manager.options.ajaxCompleteCallback(data, manager);
                });
        }

        var objectToFormData = function (obj, form, namespace) {
            var fd = form || new FormData();
            var formKey;

            for (var property in obj) {
                if (!obj.hasOwnProperty(property) || !obj[property])
                    continue;
                var formKey = namespace ? namespace + '[' + property + ']' : property;

                if (obj[property] instanceof Date) {
                    form.append(formKey, obj[property].toISOString());
                } else if (typeof obj[property] === 'object' && !(obj[property] instanceof File)) {
                    objectToFormData(obj[property], fd, formKey);
                } else {
                    fd.append(formKey, obj[property]);
                }
            }

            return fd;
        }

        return manager;
    }

    return container;

}(CFSRemark || {});