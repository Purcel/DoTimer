# Improve Language Translations
Thank you for taking the time to contribute to DoTimer! :sparkles:

If you know any of the following languages and would like to help us improve the translations for them, please follow the instructions below.

## How to improve the translations
1. Each language has a [`values`](https://github.com/Purcel/DoTimer/tree/main/app/src/main/res) directory containing the language code, and the `strings.xml` file contains the translations. Examples:
   - Romanian translation files are located within `app/src/main/res/values-ro/strings.xml`
   - Japanese translation files are located within `app/src/main/res/values-jp/strings.xml`
2. If you notice anything which should be fixed, you should create a PR containing the improved language suggestions.
   - Make the changes to the relevant `strings.xml` file
   - Test the app in the language you've improved, and ensure the changed strings look good
   - Create a PR. As well as the changes themselves, explaining _why_ the changes are an improvement helps a lot.

# Add a new language
If you know a language that is not on the list, please follow the instructions below.

## List of languages
1. English (en)

## How to add a new language
### The hard way
Create a `values-xx` folder in `app/src/main/res`, copy the `strings.xml` file from `values` in the new folder, then you can translate the `strings.xml` from the `values-xx`. Or you can use the Android Studio method.

>**_Note:_**
>
>Each language has a [`values`](https://github.com/Purcel/DoTimer/tree/main/app/src/main/res) directory containing the language code, and the `strings.xml` file contains the translations. Examples:
> - Japanese translation files are located within `app/src/main/res/values-jp/strings.xml`

### The easy way
You can add the translation in the [Discussions](https://github.com/Purcel/DoTimer/discussions) section, [Translations](https://github.com/Purcel/DoTimer/discussions/categories/translations) category.
