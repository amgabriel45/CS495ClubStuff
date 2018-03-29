var CFSRemark = function (container) {

    container.createDynamicList = function (selector, prefix, options) {
        var container = {};
        var options = options || {};

        //defaults
        if (!options.hasOwnProperty('allowDuplicates'))
            options.allowDuplicates = true;

        container.addField = function () {
            var list = $(selector);
            var isFirst = list.find('li').length == 0;
            var last = list.find('.dynamictext').last();
            if (isFirst || last.val() != "") {
                list.find('.hidden').removeClass('hidden');
                list.append('<li>\
                    <div class="input-group" style="display:inline-table">\
                        <div class="form-control-wrap">\
                            <input type="hidden" data-field="' + prefix + 'Id" val="-1" />\
                            <input type="text" style="display:inline;" class="form-control empty dynamictext" data-field="' + prefix + 'Data" />\
                        </div>\
                        <span class="input-group-btn hidden">\
                            <button class="btn btn-icon btn-danger md-delete" type="button"></button>\
                        </span>\
                    </div>\
                </li>');
                last = list.find('.dynamictext').last();
            }
            last.focus();

            if (isFirst) {
                var newFirst = $(selector).filter('li:first');

                newFirst.addClass('floating');
                newFirst.addClass('firstli');
                newFirst.find('form-control-wrap').append('<label class="form-control-label floating-label">' + prefix + '</label>');
            }

            return last;
        }

        container.collectValues = function () {
            var data = [];
            var list = $(selector);
            $.each(list.find('li'), function (i, item) {
                var id = $(item).find('[data-field="' + prefix + 'Id"]').val();
                var value = $(item).find('[data-field="' + prefix + 'Data"]').val();
                if (value != "") {
                    var obj = {};
                    obj.Id = id;
                    obj.Data = value;
                    data.push(obj);
                }
            });
            return data;
        }

        container.prepopulate = function (arr) {
            container.clearList(false);
            arr.forEach(function (d) {
                if (d != null) {
                    var field = container.addField();
                    var li = field.closest('li');
                    li.find('[data-field="' + prefix + 'Id"]').val(d.Id);
                    li.find('[data-field="' + prefix + 'Data"]').removeClass('empty').val(d.Data);
                }
            })
            container.addField();
        }

        container.addValue = function (newData) {
            if (newData != null) {
                if (!options.allowDuplicates) {
                    var isDuplicate = false;
                    $.each(container.collectValues(), function (i, item) {
                        if (item.Id == newData.Id)
                            isDuplicate = true;
                    })
                    if (isDuplicate)
                        return;
                }
                var field = container.addField();
                var li = field.closest('li');
                li.find('[data-field="' + prefix + 'Id"]').val(newData.Id);
                li.find('[data-field="' + prefix + 'Data"]').removeClass('empty').val(newData.Data);
            }
        }

        container.clearList = function (shouldAddNewField) {
            $(selector).find('li').remove();
            if (shouldAddNewField)
                container.addField();
        }

        $(selector).on("keydown", "input.dynamictext", function (e) {
            var keyCode = e.keyCode || e.which;

            if (keyCode == 9 || keyCode == 13) {
                e.preventDefault();

                container.addField();
            }
        });
        $(selector).on("click", "button.md-delete", function (e) {
            e.preventDefault();

            var thisLi = $(this).closest("li");
            var updateNewFirst = thisLi.hasClass('firstli');

            $(this).closest("li").remove();

            if ($(selector).find('li').length == 0) {
                $(selector).append('<li class="floating firstli">\
                    <div class="input-group" style="display:inline-table">\
                        <div class="form-control-wrap">\
                            <input type="hidden" data-field="' + prefix + 'Id" />\
                            <input type="text" style="display:inline;" class="form-control empty dynamictext" data-field="' + prefix + 'Data" />\
                            <label class="form-control-label floating-label">' + prefix + '</label>\
                        </div>\
                        <span class="input-group-btn hidden">\
                            <button class="btn btn-icon btn-danger md-delete" type="button"></button>\
                        </span>\
                    </div>\
                </li>'
                    );
            } else if (updateNewFirst) {
                var newFirst = $(selector).filter('li:first');

                newFirst.addClass('floating');
                newFirst.addClass('firstli');
                newFirst.find('form-control-wrap').append('<label class="form-control-label floating-label">' + prefix + '</label>');
            }
        });

        return container;
    };

    return container;

}(CFSRemark || {});