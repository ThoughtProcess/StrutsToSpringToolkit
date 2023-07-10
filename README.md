# StrutsToSpringToolkit
A collection of tools to automate the process of migrating an Apache Struts 1.x website to Spring.

## JspConverter
A command-line tool which converts a variety of Struts 1.x tags to their JSTL or Spring JSP Tag Library or Spring Form Tag Library equivalents.

### Currently supported tags:
* bean:message
* bean:write
* html:checkbox
* html:errors
* html:form
* html:hidden
* html:multibox
* html:option
* html:options
* html:optionsCollection
* html:radio
* html:select
* html:text
* html:textarea
* logic:empty
* logic:equal
* logic:iterate
* logic:notEmpty
* logic:notEqual
* logic:present

...and plenty more to come.

This project was inspired by [this article](https://www.shorterpost.com/2021/03/how-to-convert-struts-tags-to-jstl-spring.html]) and an all-rights-reserved example tool, [Struts2Jstl](https://github.com/ShorterPost/Struts2Jstl). 

### Usage
1. Unzip the distribution package (.tar or .zip, depending on your platform)
2. Run `jspConverter [FILE]`
3. By default, the tool will add the suffix `_converted` onto the file name.  For example, `foo.jsp` will be processed and saved as `foo_converted.jsp`.  If you'd like to overwrite the original file, just add the argument `--replace` after the file name.