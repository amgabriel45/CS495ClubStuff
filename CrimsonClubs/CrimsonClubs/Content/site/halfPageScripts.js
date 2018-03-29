var CFSRemark = function (container) {

    container.HalfPageManager = function (name, selector, options) {
        var manager = container.PageManager(name, selector, options);

        manager.show = function (title) {
            if (title)
                $('#bottomTitle').text(title);
            $('#topPage').removeClass('open-full-panel').addClass('open-half-panel');
            $('#bottomPage').removeClass('close-panel').addClass('open-half-panel');
            $(manager.selector).trigger('CFS.page.show');
        }
        manager.hide = function () {
            $('#topPage').removeClass('open-half-panel').addClass('open-full-panel');
            $('#bottomPage').removeClass('open-half-panel').addClass('close-panel');
            manager.removeRowHighlight();
            $(manager.selector).trigger('CFS.page.hide');
        }

        return manager;
    }

    container.HalfPageForm = function (name, selector, fields, options) {
        var pageManager = container.HalfPageManager(name, selector, options);
        var manager = container.FormManager(selector, fields, options, pageManager);

        var saveSelector = ".saveButton";
        var deleteSelector = ".deleteButton";
        var createSelector = ".createButton";
        var closeSelector = ".closeBottom";
        manager.setupListeners = function () {
            $(manager.selector).find(saveSelector).on("click", manager.saveFunction);
            $(manager.selector).find(deleteSelector).on("click", manager.deleteFunction);
            $(manager.selector).find(createSelector).on("click", manager.createFunction);
            $(manager.selector).find(closeSelector).on("click", manager.closeFunction);
        }

        manager.closeFunction = function () {
            manager.pageManager.hide();
        }
        manager.createFunction = function () {
            manager.clearData();
            manager.pageManager.showEdit("Create " + manager.pageManager.name);
        }
        manager.saveFunction = function () {
            var l = Ladda.create($(saveSelector)[0]);
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
            var l = Ladda.create($('#deleteButton')[0]);
            l.start();
            var id = manager.options.deleteIdSelector();

            manager.ajaxFunction(
                manager.options.deleteURL + "?id=" + id,
                { },
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