package org.opengamestudio




data class AppContext(
    var baseURL: String = "",
    var cfg: Map<String, String> = mapOf(),
    var converterInput: String = "",
    var converterOutput: String = "",
    var didClickEditorTab: Boolean = false,
    var didClickFilesTab: Boolean = false,
    var didClickRenderTab: Boolean = false,
    var didClickSaveBtn: Boolean = false,
    var didLaunch: Boolean = false,
    var didResize: Boolean = false,
    var didSaveEditedFiles: Boolean = false,
    var didSaveFile: Boolean = false,
    var didSaveRenderedFile: Boolean = false,
    var editedContents: String = "",
    var editedFileContents: Map<String, String> = mapOf(),
    var editorContents: String = "",
    var header: Array<String> = arrayOf(),
    var inputDirFiles: Map<Int, Array<FSFile>> = mapOf(),
    var inputDirs: Array<String> = arrayOf(),
    var inputMDFiles: Map<Int, Array<String>> = mapOf(),
    var installEditor: Boolean = false,
    var installMDConverter: Boolean = false,
    var itemTemplates: Map<Int, String> = mapOf(),
    var listInputDirId: Int = 0,
    var page: Page = Page(),
    var projectPath: String = "",
    var readFile: String = "",
    var readFileContents: String = "",
    var renderPage: String = "",
    var renderedFile: String = "",
    var request: NetRequest = NetRequest(),
    var resizeEditor: Boolean = false,
    var resizeRenderer: Boolean = false,
    var response: NetResponse = NetResponse(),
    var responseError: NetResponse = NetResponse(),
    var saveFileId: Int = 0,
    var saveFiles: Array<String> = arrayOf(),
    var selectedFileId: Array<Int> = arrayOf(),
    var selectedFileName: String = "",
    var selectedTabId: Int = 0,
    var splashTimeout: Int = 0,
    override var recentField: String = "",
): CLDContext {
    override fun <T> field(name: String): T {
        if (name == "baseURL") {
            return baseURL as T
        } else if (name == "cfg") {
            return cfg as T
        } else if (name == "converterInput") {
            return converterInput as T
        } else if (name == "converterOutput") {
            return converterOutput as T
        } else if (name == "didClickEditorTab") {
            return didClickEditorTab as T
        } else if (name == "didClickFilesTab") {
            return didClickFilesTab as T
        } else if (name == "didClickRenderTab") {
            return didClickRenderTab as T
        } else if (name == "didClickSaveBtn") {
            return didClickSaveBtn as T
        } else if (name == "didLaunch") {
            return didLaunch as T
        } else if (name == "didResize") {
            return didResize as T
        } else if (name == "didSaveEditedFiles") {
            return didSaveEditedFiles as T
        } else if (name == "didSaveFile") {
            return didSaveFile as T
        } else if (name == "didSaveRenderedFile") {
            return didSaveRenderedFile as T
        } else if (name == "editedContents") {
            return editedContents as T
        } else if (name == "editedFileContents") {
            return editedFileContents as T
        } else if (name == "editorContents") {
            return editorContents as T
        } else if (name == "header") {
            return header as T
        } else if (name == "inputDirFiles") {
            return inputDirFiles as T
        } else if (name == "inputDirs") {
            return inputDirs as T
        } else if (name == "inputMDFiles") {
            return inputMDFiles as T
        } else if (name == "installEditor") {
            return installEditor as T
        } else if (name == "installMDConverter") {
            return installMDConverter as T
        } else if (name == "itemTemplates") {
            return itemTemplates as T
        } else if (name == "listInputDirId") {
            return listInputDirId as T
        } else if (name == "page") {
            return page as T
        } else if (name == "projectPath") {
            return projectPath as T
        } else if (name == "readFile") {
            return readFile as T
        } else if (name == "readFileContents") {
            return readFileContents as T
        } else if (name == "renderPage") {
            return renderPage as T
        } else if (name == "renderedFile") {
            return renderedFile as T
        } else if (name == "request") {
            return request as T
        } else if (name == "resizeEditor") {
            return resizeEditor as T
        } else if (name == "resizeRenderer") {
            return resizeRenderer as T
        } else if (name == "response") {
            return response as T
        } else if (name == "responseError") {
            return responseError as T
        } else if (name == "saveFileId") {
            return saveFileId as T
        } else if (name == "saveFiles") {
            return saveFiles as T
        } else if (name == "selectedFileId") {
            return selectedFileId as T
        } else if (name == "selectedFileName") {
            return selectedFileName as T
        } else if (name == "selectedTabId") {
            return selectedTabId as T
        } else if (name == "splashTimeout") {
            return splashTimeout as T
        }
        return "unknown-field-name" as T
    }

    override fun selfCopy(): CLDContext {
        return this.copy()
    }

    override fun setField(
        name: String,
        value: Any?
    ) {
        if (name == "baseURL") {
            baseURL = value as String
        } else if (name == "cfg") {
            cfg = value as Map<String, String>
        } else if (name == "converterInput") {
            converterInput = value as String
        } else if (name == "converterOutput") {
            converterOutput = value as String
        } else if (name == "didClickEditorTab") {
            didClickEditorTab = value as Boolean
        } else if (name == "didClickFilesTab") {
            didClickFilesTab = value as Boolean
        } else if (name == "didClickRenderTab") {
            didClickRenderTab = value as Boolean
        } else if (name == "didClickSaveBtn") {
            didClickSaveBtn = value as Boolean
        } else if (name == "didLaunch") {
            didLaunch = value as Boolean
        } else if (name == "didResize") {
            didResize = value as Boolean
        } else if (name == "didSaveEditedFiles") {
            didSaveEditedFiles = value as Boolean
        } else if (name == "didSaveFile") {
            didSaveFile = value as Boolean
        } else if (name == "didSaveRenderedFile") {
            didSaveRenderedFile = value as Boolean
        } else if (name == "editedContents") {
            editedContents = value as String
        } else if (name == "editedFileContents") {
            editedFileContents = value as Map<String, String>
        } else if (name == "editorContents") {
            editorContents = value as String
        } else if (name == "header") {
            header = value as Array<String>
        } else if (name == "inputDirFiles") {
            inputDirFiles = value as Map<Int, Array<FSFile>>
        } else if (name == "inputDirs") {
            inputDirs = value as Array<String>
        } else if (name == "inputMDFiles") {
            inputMDFiles = value as Map<Int, Array<String>>
        } else if (name == "installEditor") {
            installEditor = value as Boolean
        } else if (name == "installMDConverter") {
            installMDConverter = value as Boolean
        } else if (name == "itemTemplates") {
            itemTemplates = value as Map<Int, String>
        } else if (name == "listInputDirId") {
            listInputDirId = value as Int
        } else if (name == "page") {
            page = value as Page
        } else if (name == "projectPath") {
            projectPath = value as String
        } else if (name == "readFile") {
            readFile = value as String
        } else if (name == "readFileContents") {
            readFileContents = value as String
        } else if (name == "renderPage") {
            renderPage = value as String
        } else if (name == "renderedFile") {
            renderedFile = value as String
        } else if (name == "request") {
            request = value as NetRequest
        } else if (name == "resizeEditor") {
            resizeEditor = value as Boolean
        } else if (name == "resizeRenderer") {
            resizeRenderer = value as Boolean
        } else if (name == "response") {
            response = value as NetResponse
        } else if (name == "responseError") {
            responseError = value as NetResponse
        } else if (name == "saveFileId") {
            saveFileId = value as Int
        } else if (name == "saveFiles") {
            saveFiles = value as Array<String>
        } else if (name == "selectedFileId") {
            selectedFileId = value as Array<Int>
        } else if (name == "selectedFileName") {
            selectedFileName = value as String
        } else if (name == "selectedTabId") {
            selectedTabId = value as Int
        } else if (name == "splashTimeout") {
            splashTimeout = value as Int
        }
    }
}



data class FSFile(
    var isFile: Boolean = false,
    var path: String = "",
) {}



data class NetRequest(
    var body: String = "",
    var method: String = "",
    var url: String = "",
) {}



data class NetResponse(
    var contents: String = "",
    var req: NetRequest = NetRequest(),
) {}



data class Page(
    var contents: String = "",
    var date: String = "",
    var slug: String = "",
    var title: String = "",
) {}



data class PlayContext(
    var didLaunch: Boolean = false,
    var didResetWebView: Boolean = false,
    var isPlaygroundVisible: Boolean = false,
    var url: String = "",
    override var recentField: String = "",
): CLDContext {
    override fun <T> field(name: String): T {
        if (name == "didLaunch") {
            return didLaunch as T
        } else if (name == "didResetWebView") {
            return didResetWebView as T
        } else if (name == "isPlaygroundVisible") {
            return isPlaygroundVisible as T
        } else if (name == "url") {
            return url as T
        }
        return "unknown-field-name" as T
    }

    override fun selfCopy(): CLDContext {
        return this.copy()
    }

    override fun setField(
        name: String,
        value: Any?
    ) {
        if (name == "didLaunch") {
            didLaunch = value as Boolean
        } else if (name == "didResetWebView") {
            didResetWebView = value as Boolean
        } else if (name == "isPlaygroundVisible") {
            isPlaygroundVisible = value as Boolean
        } else if (name == "url") {
            url = value as String
        }
    }
}



data class SrvContext(
    var arguments: Array<String> = arrayOf(),
    var browserDir: String = "",
    var defaultBrowserDir: String = "",
    var defaultHTTPPort: Int = 0,
    var didLaunch: Boolean = false,
    var didWriteFile: Boolean = false,
    var dirFiles: Array<FSFile> = arrayOf(),
    var httpPort: Int = 0,
    var inputDir: String = "",
    var inputHTTPPort: Int = 0,
    var listDir: String = "",
    var projectAbsPath: String = "",
    var projectDir: String = "",
    var readFile: String = "",
    var readFileContents: String = "",
    var request: NetRequest = NetRequest(),
    var response: NetResponse = NetResponse(),
    var url: String = "",
    var writeFile: Array<String> = arrayOf(),
    override var recentField: String = "",
): CLDContext {
    override fun <T> field(name: String): T {
        if (name == "arguments") {
            return arguments as T
        } else if (name == "browserDir") {
            return browserDir as T
        } else if (name == "defaultBrowserDir") {
            return defaultBrowserDir as T
        } else if (name == "defaultHTTPPort") {
            return defaultHTTPPort as T
        } else if (name == "didLaunch") {
            return didLaunch as T
        } else if (name == "didWriteFile") {
            return didWriteFile as T
        } else if (name == "dirFiles") {
            return dirFiles as T
        } else if (name == "httpPort") {
            return httpPort as T
        } else if (name == "inputDir") {
            return inputDir as T
        } else if (name == "inputHTTPPort") {
            return inputHTTPPort as T
        } else if (name == "listDir") {
            return listDir as T
        } else if (name == "projectAbsPath") {
            return projectAbsPath as T
        } else if (name == "projectDir") {
            return projectDir as T
        } else if (name == "readFile") {
            return readFile as T
        } else if (name == "readFileContents") {
            return readFileContents as T
        } else if (name == "request") {
            return request as T
        } else if (name == "response") {
            return response as T
        } else if (name == "url") {
            return url as T
        } else if (name == "writeFile") {
            return writeFile as T
        }
        return "unknown-field-name" as T
    }

    override fun selfCopy(): CLDContext {
        return this.copy()
    }

    override fun setField(
        name: String,
        value: Any?
    ) {
        if (name == "arguments") {
            arguments = value as Array<String>
        } else if (name == "browserDir") {
            browserDir = value as String
        } else if (name == "defaultBrowserDir") {
            defaultBrowserDir = value as String
        } else if (name == "defaultHTTPPort") {
            defaultHTTPPort = value as Int
        } else if (name == "didLaunch") {
            didLaunch = value as Boolean
        } else if (name == "didWriteFile") {
            didWriteFile = value as Boolean
        } else if (name == "dirFiles") {
            dirFiles = value as Array<FSFile>
        } else if (name == "httpPort") {
            httpPort = value as Int
        } else if (name == "inputDir") {
            inputDir = value as String
        } else if (name == "inputHTTPPort") {
            inputHTTPPort = value as Int
        } else if (name == "listDir") {
            listDir = value as String
        } else if (name == "projectAbsPath") {
            projectAbsPath = value as String
        } else if (name == "projectDir") {
            projectDir = value as String
        } else if (name == "readFile") {
            readFile = value as String
        } else if (name == "readFileContents") {
            readFileContents = value as String
        } else if (name == "request") {
            request = value as NetRequest
        } else if (name == "response") {
            response = value as NetResponse
        } else if (name == "url") {
            url = value as String
        } else if (name == "writeFile") {
            writeFile = value as Array<String>
        }
    }
}
