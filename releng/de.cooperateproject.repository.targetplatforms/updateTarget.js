var File = Java.type("java.io.File");
var PrintWriter = Java.type("java.io.PrintWriter");
var Scanner = Java.type('java.util.Scanner');
var URL = Java.type('java.net.URL');

var CONTENT_JAR_URL = 'https://dl.bintray.com/cooperate-project/CooperateModelingEnvironment/updatesite/latest/content.jar';
var FEATURE_NAME = 'de.cooperateproject.modeling.textual.metamodels.feature';

function escapeRegExp (string) {
    return string.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
}

function determineVersion(url, featureName) {
	var contentURL = new URL("jar:" + url + "!/content.xml");
	var inputStream = contentURL.openStream();
	try {
		var s = new Scanner(inputStream).useDelimiter('\\A');
		var result = s.hasNext() ? s.next() : '';
		var versionRegex = new RegExp("id='" + escapeRegExp(featureName) + "\\.feature\\.jar' version='([0-9.]+)'");
		var match = versionRegex.exec(result);
		var foundVersion = match[1];
		if (foundVersion === undefined) {
			return;
		}
		return foundVersion;
	} finally {
		inputStream.close();
	}	
}

function getFileContent(filePath) {
	return new Scanner(new File(filePath)).useDelimiter('\\Z').next();
}

function writeFileContent(filePath, newContent) {
	var writer = new PrintWriter(new File(filePath));
	try {
		writer.print(newContent);
	} finally {
		writer.close();
	}
}

function replaceVersion(targetFilePath, featureName, newVersion) {
	var fileContent = getFileContent(targetFilePath);
	var featureRegex = new RegExp('id="' + escapeRegExp(featureName) + '\\.feature\\.group" version="[^"]+"');
	fileContent = fileContent.replace(featureRegex, 'id="' + featureName + '.feature.group" version="' + newVersion + '"');
	writeFileContent(targetFilePath, fileContent);
}

function main(arguments) {
	if (arguments.length != 2) {
		print('Call the script with the following arguments:');
		print('\tpath to target file');
		print('\tstring consisting of the word "master"');
		return;
	}
	var targetFilePath = arguments[0];
	var foundVersion = determineVersion(CONTENT_JAR_URL, FEATURE_NAME);
	replaceVersion(targetFilePath, FEATURE_NAME, foundVersion);
}

main(arguments)