var CFSRemark = function (container) {

    container.ModalManager = function (name, selector, options) {
        var manager = container.PageManager(name, selector, options);

        manager.show = function (title) {
            if (title)
                $(manager.selector).find('.modal-title').text(title);
            $(manager.selector).modal('show');
            $(manager.selector).trigger('CFS.page.show');
        }
        manager.hide = function () {
            $(manager.selector).modal('hide');
            $('tr.highlight').removeClass('highlight');
            $(manager.selector).trigger('CFS.page.hide');
        }

        return manager;
    }

    container.ModalForm = function (name, selector, fields, options) {
        var pageManager = container.ModalManager(name, selector, options);
        var manager = container.FormManager(selector, fields, options, pageManager);

        var saveSelector = ".saveButton";
        var deleteSelector = ".deleteButton";
        var createSelector = ".createButton";
        var closeSelector = ".closeButton";
        var cancelSelector = ".cancelButton";
        var editSelector = ".editButton";
        var delSelector = ".delButton";
        manager.setupListeners = function () {
            $(manager.selector).find(saveSelector).on("click", manager.saveFunction);
            $(manager.selector).find(deleteSelector).on("click", manager.deleteFunction);
            $(manager.selector).find(createSelector).on("click", function () { manager.createFunction("Create " + manager.pageManager.name); });
            $(manager.selector).find(closeSelector).on("click", manager.closeFunction);
            $(manager.selector).find(cancelSelector).on("click", manager.closeFunction);
            if (manager.pageManager) {
                $(manager.selector).find(editSelector).on("click", function () { manager.pageManager.showEdit("Edit " + manager.pageManager.name); });
                $(manager.selector).find(delSelector).on("click", function () { manager.pageManager.showDelete("Delete " + manager.pageManager.name); });
            }
        }

        manager.closeFunction = function () {
            manager.pageManager.hide();
        }
        manager.createFunction = function () {
            manager.clearData();
            manager.pageManager.showEdit("Create " + manager.pageManager.name);
        }
        manager.showFunction = function (data) {
            manager.putData(data);
            manager.pageManager.show("View" + manager.pageManager.name);
        }
        manager.saveFunction = function () {
            var l = Ladda.create($(manager.selector).find(saveSelector)[0]);
            l.start();
            var data = manager.getData();

            manager.ajaxFunction(
                manager.options.saveURL,
                data,
                "Successfully saved.",
                "Error occurred. Please check form for error messages.",
                function (data) { manager.pageManager.showView(); $(manager.selector).trigger("CFS.form.edit.success"); },
                function (data) { $(manager.selector).trigger("CFS.form.edit.failure"); },
                function (data) { manager.putData(data); l.stop(); });
        }
        manager.deleteFunction = function () {
            var l = Ladda.create($(manager.selector).find(deleteSelector)[0]);
            l.start();
            var id = manager.options.deleteIdSelector();

            manager.ajaxFunction(
                manager.options.deleteURL + "?id=" + id,
                {},
                "Successfully deleted.",
                "Error occurred. Failed to delete item.",
                function (data) { manager.pageManager.hide(); $(manager.selector).trigger("CFS.form.delete.success"); },
                function (data) { $(manager.selector).trigger("CFS.form.delete.failure"); },
                function (data) { l.stop(); });
        }

        return manager;
    }

    return container;

}(CFSRemark || {});