# Exported tables with a source-file breakdown

This document describes preparing a self-contained HTML file for studying source
code in a browser: a line-by-line table with explanations and, if needed, a
separate version after the code has been changed.

## Output file

1. Write the result to `docs/<SourceFileName>.html`, named after the source file
   it documents (for example `docs/RaylibGLView.html` for `RaylibGLView.swift`).
2. The document is the source of truth for the revision it names: capture the
   file at the exact Git revision requested and record that revision in the
   header, rather than documenting the current working tree.

## Source code and explanations

1. Use the exact version of the file the user requested. For the version prior to
   a fix, obtain the contents from the corresponding Git revision rather than
   substituting the current file. State the file name and the source revision in
   the document.
2. Keep every line of the source, their order, the original numbers, blank lines
   and indentation. Do not shorten expressions or reformat code for the sake of
   the table.
3. Create three columns: line number, source line, explanation.
4. If the breakdown was already given in the conversation, carry over the original
   explanations verbatim. Do not replace them with an abridged retelling or new
   wording. Preserve the highlighting and notation from the code.
5. Do not add obvious explanations such as "Ends the block", "Continuation of the
   declaration" or descriptions of blank lines. Leave such cells empty.
6. Place the explanation for several lines in a single vertically merged cell of
   the third column via `rowspan`. Keep the individual numbers and code of each
   line in the first two columns.

## HTML formatting

1. The result is a separate downloadable `.html` file opened in a browser. Keep
   all syntax highlighting and styling inside the file, with no external
   libraries, fonts, network requests or other external dependencies.
2. Put the code in `<pre><code>`, preserving the original spaces and indentation.
   Escape HTML special characters so that the displayed code matches the source.
3. Include syntax highlighting for the relevant language. For Swift, distinguish
   keywords, types, strings, comments, numbers and directives.
4. Make the second and third columns equal in width. Keep the number column
   narrow; it does not take part in splitting the remaining space in half.
5. Allow long code lines to wrap while preserving the original indentation:
   `white-space: pre-wrap` and `overflow-wrap: anywhere`. Do not require
   horizontal scrolling to read long expressions.
6. Do not use alternating coloured row shading.
7. Align the horizontal separators in the code and explanation columns. When an
   explanation is merged, remove the internal lines between the corresponding
   code lines: the borders must separate the same semantic groups.
8. Do not draw horizontal lines before completely blank lines: visually they abut
   the preceding fragment.
9. Do not draw horizontal lines before lines with an empty explanation, including
   non-empty code lines. They visually continue the previous group.
10. Preserve the readability of the highlighting, the wrapping and the equal
    column widths when changing the formatting; editing one property must not
    undo the others.

## Separate version after the fix

1. On request, create a second HTML file with the changed version of the source.
   Keep the first file with the version prior to the fix.
2. Use the line numbers of the new version. Compare it against the explicitly
   stated source revision and determine the lines that were actually added and
   changed.
3. Highlight in green only the numbers of the new and changed lines in the first
   column. Do not colour entire table rows, the code background or the
   explanations. Keep the normal code syntax highlighting.
4. For unchanged fragments, keep the original explanations verbatim. For the new
   code, add explanations of its purpose and how it works.
5. Apply the same requirements for indentation, wrapping, column widths,
   explanation merging and horizontal separators to both files.

## Verification and handoff

1. Verify that the lines and numbering match the chosen version of the source.
2. Verify that the original explanations are verbatim and that there are no
   obvious boilerplate comments instead of substantive explanations.
3. Verify that `rowspan` is correct, the column borders line up, the code wraps,
   there is no striped shading, and changes are highlighted only in the number
   column.
4. Make sure the HTML is fully self-contained, and give the user a link to the
   file. When updating an already open document, tell the user the page needs to
   be refreshed.
