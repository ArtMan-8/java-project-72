@template.layout.page(content = @`
    <section class="bg-gray-700 p-10">
        <header class="max-w-4xl m-auto">
            <h1 class="text-white text-7xl">Анализатор страниц</h1>
            <p class="text-white text-2xl">Бесплатно проверяйте сайты на SEO пригодность</p>
        </header>

        <form action="/urls" method="post" class="flex flex-wrap items-center justify-between max-w-4xl m-auto">
            <label for="url-input" class="sr-only">Ссылка</label>
            <input id="url-input" name="url" type="text" required placeholder="Ссылка" class="bg-white w-3/4 px-5 py-4 rounded-md"/>

            <button type="submit" class="text-white text-lg bg-blue-600 w-1/5 px-4 py-3 rounded-md cursor-pointer hover:bg-blue-700">Проверить</button>

            <p class="text-gray-400 text-base">Пример: https://www.example.com</p>
        </form>
    </section>
`)