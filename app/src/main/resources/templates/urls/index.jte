@import hexlet.code.dto.urls.UrlsPage

@param UrlsPage page

@template.layout.page(content = @`
    <section class="p-10">
        <h1 class="text-5xl mb-4">Сайты</h1>

        <table class="w-full border-collapse border border-gray-400">
            <thead>
                <tr>
                    <th class="w-1/8 border border-gray-400 p-2">ID</th>
                    <th class="w-4/8 border border-gray-400 p-2">Имя</th>
                    <th class="w-2/8 border border-gray-400 p-2">Последняя проверка</th>
                    <th class="w-1/8 border border-gray-400 p-2">Код ответа</th>
                </tr>
            </thead>
            <tbody>
                @for(var url : page.getUrls())
                    <tr>
                        <th class="w-1/8 border border-gray-400 p-2">${url.getId()}</th>
                        <th class="w-4/8 border border-gray-400 p-2">
                            <a href="/urls/${url.getId()}" class="underline text-blue-400">${url.getName()}</a>
                        </th>
                        <th class="w-2/8 border border-gray-400 p-2"></th>
                        <th class="w-1/8 border border-gray-400 p-2"></th>
                    </tr>
                @endfor
            </tbody>
        </table>
    </section>
`)