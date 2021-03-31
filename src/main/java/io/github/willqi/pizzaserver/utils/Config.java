package io.github.willqi.pizzaserver.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Config extends ConfigGroup {

    public void load(InputStream stream) {
        try {

            Reader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder keyBuilder = new StringBuilder(); // can represent property name or array element
            StringBuilder valueBuilder = new StringBuilder();

            String groupOrArrayName = null;
            boolean decidingIfIsGroupOrArray = false;
            ConfigGroup currentGroup = this;
            LastLineArrayStatus lastLineArrayStatus = LastLineArrayStatus.FALSE;    // Used to parse arrays
            // as it is difficult to distinguish them from
            // property value lines

            ConfigParseType parseType = ConfigParseType.PROPERTY;
            int charVal;
            int indentationLevel = 0;       // Expected indentation for current ConfigGroup children
            int lineIndentationLevel = 0;   // Indentation of current line.
            while ((charVal = reader.read()) != -1) {
                char c = (char) charVal;

                boolean isNewLineCharacter = (c == '\n') || (c == '\r');
                if (isNewLineCharacter && keyBuilder.length() == 0 && parseType != ConfigParseType.COMMENT) {
                    continue;
                } else if (isNewLineCharacter) {
                    if (parseType == ConfigParseType.COMMENT) {
                        parseType = ConfigParseType.PROPERTY;
                    } else if (keyBuilder.length() > 0) {
                        // Parsing actual property data
                        if (parseType == ConfigParseType.PROPERTY) {
                            throw new RuntimeException("Invalid configuration. Found newline character before colon.");
                        } else {
                            // Figure out which ConfigGroup this property belongs to based on the indentation

                            if (lineIndentationLevel % 2 != 0) {
                                throw new RuntimeException("Invalid configuration. Line indentation is not a multiple of 2.");
                            } else if (lineIndentationLevel < indentationLevel) {
                                // This belongs to a lower child. Get that ConfigGroup
                                if (lastLineArrayStatus == LastLineArrayStatus.LAST_LINE) {
                                    indentationLevel -= 2;
                                } else {
                                    while (lineIndentationLevel < indentationLevel) {
                                        currentGroup = currentGroup.getParentGroup();
                                        indentationLevel -= 2;
                                    }
                                }
                            }
                        }

                        if (parseType == ConfigParseType.LIST) {
                            // Add this element to the parent array
                            List<String> list = currentGroup.getStringList(groupOrArrayName);
                            list.add(keyBuilder.toString().trim());
                            currentGroup.setStringList(groupOrArrayName, list);
                            lastLineArrayStatus = LastLineArrayStatus.THIS_LINE;
                        } else if (valueBuilder.length() == 0) {
                            // We're creating a group or array
                            indentationLevel += 2;
                            groupOrArrayName = keyBuilder.toString();
                            decidingIfIsGroupOrArray = true;
                        } else {
                            currentGroup.setString(keyBuilder.toString(), valueBuilder.toString());
                        }
                        keyBuilder = new StringBuilder();
                        valueBuilder = new StringBuilder();
                        parseType = ConfigParseType.PROPERTY;

                        if (lastLineArrayStatus == LastLineArrayStatus.THIS_LINE) {
                            lastLineArrayStatus = LastLineArrayStatus.LAST_LINE;
                        }

                        lineIndentationLevel = 0;

                    }
                } else if (c == '#' && keyBuilder.length() == 0) {
                    parseType = ConfigParseType.COMMENT;
                } else if (parseType != ConfigParseType.COMMENT) {

                    if (c == ' ' && keyBuilder.length() == 0 && parseType != ConfigParseType.LIST) {
                        // Indentation at start of line

                        if (lineIndentationLevel > indentationLevel) {
                            throw new RuntimeException("Invalid configuration. Indentation exceeded correct amount of spaces");
                        } else {
                            lineIndentationLevel++;
                        }
                    } else if (c == '-' && keyBuilder.length() == 0 && parseType != ConfigParseType.LIST) {
                        if (indentationLevel == 0) {
                            throw new RuntimeException("Invalid configuration. Attempted to create array without proper indentation");
                        } else {
                            if (decidingIfIsGroupOrArray) {
                                currentGroup.setStringList(groupOrArrayName, new ArrayList<>());
                                decidingIfIsGroupOrArray = false;
                            }
                            parseType = ConfigParseType.LIST;
                        }
                    } else if (parseType == ConfigParseType.LIST) {
                        keyBuilder.append(c);
                    } else if (parseType == ConfigParseType.PROPERTY) {
                        if (decidingIfIsGroupOrArray) {
                            // A property ONLY belongs to a group.
                            ConfigGroup newGroup = new ConfigGroup(groupOrArrayName, currentGroup);
                            currentGroup.setGroup(groupOrArrayName, newGroup);
                            currentGroup = newGroup;
                            decidingIfIsGroupOrArray = false;
                        }

                        // Parsing property name
                        if (c == ':') {
                            parseType = ConfigParseType.VALUE;
                        } else {
                            keyBuilder.append(c);
                        }
                    } else {
                        // Parsing value
                        if ((valueBuilder.length() == 0 && c != ' ') || (valueBuilder.length() > 0)) {
                            valueBuilder.append(c);
                        }
                    }

                }
            }

            // While the loop has ended, the propertyBuilder and valueBuilder have one last value we need to store
            if (parseType != ConfigParseType.COMMENT) {

                if (keyBuilder.length() > 0) {
                    if (parseType == ConfigParseType.PROPERTY) {
                        throw new RuntimeException("Invalid configuration. Found newline character before colon.");
                    } else {
                        // Figure out which ConfigGroup this property belongs to based on the indentation

                        if (lineIndentationLevel % 2 != 0) {
                            throw new RuntimeException("Invalid configuration. Line indentation is not a multiple of 2.");
                        } else if (lineIndentationLevel < indentationLevel) {
                            // This belongs to a lower child. Get that ConfigGroup
                            if (lastLineArrayStatus != LastLineArrayStatus.LAST_LINE) {
                                while (lineIndentationLevel < indentationLevel) {
                                    currentGroup = currentGroup.getParentGroup();
                                    indentationLevel -= 2;
                                }
                            }
                        }
                    }

                    if (parseType == ConfigParseType.LIST) {
                        // Add this element to the parent array
                        List<String> list = currentGroup.getStringList(groupOrArrayName);
                        list.add(keyBuilder.toString().trim());
                        currentGroup.setStringList(groupOrArrayName, list);
                    } else if (parseType == ConfigParseType.VALUE) {
                        currentGroup.setString(keyBuilder.toString(), valueBuilder.toString());
                    }
                }

            }

            stream.close();
        } catch (IOException | RuntimeException exception) {
            throw new RuntimeException(exception);
        }

    }


    public void save(String saveLocation) {
        FileOutputStream stream;
        try {
            stream = new FileOutputStream(saveLocation);
            this.save(stream);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    public void save(OutputStream stream) {
        try {
            writeConfigGroup(this, stream, 0);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        } finally {
            try {
                stream.close();
            } catch (IOException exception) {}
        }
    }

    private static void writeConfigGroup(ConfigGroup group, OutputStream stream, int indentation) throws IOException {
        Map<String, Object> properties = group.getProperties();
        for (Map.Entry<String, Object> property : properties.entrySet()) {
            if (property.getValue() instanceof List) {
                stream.write(convertStringToBytes(property.getKey() + ":\n", indentation));
                for (Object element : ((List)property.getValue())) {
                    stream.write(convertStringToBytes("- " + element + "\n", indentation + 2));
                }
            } else if (property.getValue() instanceof ConfigGroup) {
                stream.write(convertStringToBytes(property.getKey() + ":\n", indentation));
                writeConfigGroup((ConfigGroup)property.getValue(), stream, indentation + 2);
            } else {
                stream.write(convertStringToBytes(property.getKey() + ": " + property.getValue() + "\n", indentation));
            }
        }
    }

    private static byte[] convertStringToBytes(String input, int indentation) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < indentation; i++) {
            builder.append(' ');
        }
        builder.append(input);
        return builder.toString().getBytes(StandardCharsets.UTF_8);
    }

    private enum ConfigParseType {
        COMMENT,
        PROPERTY,
        VALUE,
        LIST
    }

    private enum LastLineArrayStatus {
        FALSE,      // This and the last line were not an array
        THIS_LINE,  // This line is a array element!
        LAST_LINE   // The last line was a array element!
    }

}
