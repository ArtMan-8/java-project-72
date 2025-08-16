@import gg.jte.Content
@import hexlet.code.dto.BasePage

@param Content content
@param BasePage page = null

<html lang="ru">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
    <title>Анализатор страниц</title>
</head>
<body class="h-dvh flex flex-col">
    <header class="flex bg-gray-700 p-4">
        <div class="flex justify-start items-baseline">
            <p class="text-white text-2xl mr-4">Анализатор страниц</p>
            <nav class="flex justify-center items-center gap-2">
                <a href="/" class="text-gray-400 hover:text-gray-200 text-base">Главная</a>
                <a href="/urls" class="text-gray-400 hover:text-gray-200 text-base">Сайты</a>
            </nav>
        </div>
    </header>

    @if(page != null && page.getFlashMessage() != null)
        <div class="w-full p-4" id="flash-container">
            <div class="max-w-4xl mx-auto">
                @if("error".equals(page.getFlashType()))
                    <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative flash-message cursor-pointer transition-opacity duration-300" id="flash-message">
                        <span class="block sm:inline">${page.getFlashMessage()}</span>
                        <span class="absolute top-0 bottom-0 right-0 px-4 py-3" onclick="closeFlashMessage()">
                            <svg class="fill-current h-6 w-6 text-red-500 hover:text-red-700" role="button" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20">
                                <title>Закрыть</title>
                                <path d="M14.348 14.849a1.2 1.2 0 0 1-1.697 0L10 11.819l-2.651 3.029a1.2 1.2 0 1 1-1.697-1.697l2.758-3.15-2.759-3.152a1.2 1.2 0 0 1 1.697-1.697L10 8.183l2.651-3.031a1.2 1.2 0 0 1 1.697 1.697l-2.758 3.152 2.758 3.15z"/>
                            </svg>
                        </span>
                    </div>
                @elseif("success".equals(page.getFlashType()))
                    <div class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded relative flash-message cursor-pointer transition-opacity duration-300" id="flash-message">
                        <span class="block sm:inline">${page.getFlashMessage()}</span>
                        <span class="absolute top-0 bottom-0 right-0 px-4 py-3" onclick="closeFlashMessage()">
                            <svg class="fill-current h-6 w-6 text-green-500 hover:text-green-700" role="button" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20">
                                <title>Закрыть</title>
                                <path d="M14.348 14.849a1.2 1.2 0 0 1-1.697 0L10 11.819l-2.651 3.029a1.2 1.2 0 1 1-1.697-1.697l2.758-3.15-2.759-3.152a1.2 1.2 0 0 1 1.697-1.697L10 8.183l2.651-3.031a1.2 1.2 0 0 1 1.697 1.697l-2.758 3.152 2.758 3.15z"/>
                            </svg>
                        </span>
                    </div>
                @else
                    <div class="bg-blue-100 border border-blue-400 text-blue-700 px-4 py-3 rounded relative flash-message cursor-pointer transition-opacity duration-300" id="flash-message">
                        <span class="block sm:inline">${page.getFlashMessage()}</span>
                        <span class="absolute top-0 bottom-0 right-0 px-4 py-3" onclick="closeFlashMessage()">
                            <svg class="fill-current h-6 w-6 text-blue-500 hover:text-blue-700" role="button" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20">
                                <title>Закрыть</title>
                                <path d="M14.348 14.849a1.2 1.2 0 0 1-1.697 0L10 11.819l-2.651 3.029a1.2 1.2 0 1 1-1.697-1.697l2.758-3.15-2.759-3.152a1.2 1.2 0 0 1 1.697-1.697L10 8.183l2.651-3.031a1.2 1.2 0 0 1 1.697 1.697l-2.758 3.152 2.758 3.15z"/>
                            </svg>
                        </span>
                    </div>
                @endif
            </div>
        </div>
    @endif

    <main class="flex flex-col">
        ${content}
    </main>

    <footer class="bg-gray-200 mt-auto p-2">
        <div class="flex justify-center items-center">
            <a href="https://github.com/ArtMan-8" class="underline underline-offset-2 text-blue-600 hover:text-blue-900">@ArtMan-8</a>
        </div>
    </footer>

    <script>
        // Функция для закрытия flash сообщения
        function closeFlashMessage() {
            const flashContainer = document.getElementById('flash-container');
            flashContainer.style.display = 'none';
        }

        // Добавляем обработчик для закрытия по клику на сообщение
        document.addEventListener('DOMContentLoaded', function() {
            const flashMessage = document.getElementById('flash-message');
            if (flashMessage) {
                flashMessage.addEventListener('click', closeFlashMessage);
            }
        });
    </script>
</body>
</html>