@import hexlet.code.dto.UrlPage

@param UrlPage page

@template.layout.page(content = @`
    <section class="p-10">
        <h1 class="text-5xl mb-4">Сайт: ${page.getUrl().getName()}</h1>

        <div class="mb-8">
            <table class="w-full border-collapse border border-gray-400 mb-6">
                <tbody>
                    <tr>
                        <th class="w-1/4 border border-gray-400 p-2 bg-gray-100 text-left">ID</th>
                        <td class="w-3/4 border border-gray-400 p-2">${page.getUrl().getId()}</td>
                    </tr>
                    <tr>
                        <th class="w-1/4 border border-gray-400 p-2 bg-gray-100 text-left">Имя</th>
                        <td class="w-3/4 border border-gray-400 p-2">${page.getUrl().getName()}</td>
                    </tr>
                    <tr>
                        <th class="w-1/4 border border-gray-400 p-2 bg-gray-100 text-left">Дата создания</th>
                        <td class="w-3/4 border border-gray-400 p-2">
                            @if(page.getUrl().getCreatedAt() != null)
                                ${page.getUrl().getCreatedAt().toLocalDateTime().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}
                            @endif
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div>
            <h2 class="text-3xl mb-4">Проверки</h2>
            
            <!-- Кнопка запуска проверки -->
            <div class="mb-6">
                <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                    Запустить проверку
                </button>
            </div>

            <!-- Таблица проверок -->
            <table class="w-full border-collapse border border-gray-400">
                <thead>
                    <tr>
                        <th class="w-1/12 border border-gray-400 p-2 bg-gray-100">ID</th>
                        <th class="w-2/12 border border-gray-400 p-2 bg-gray-100">Код ответа</th>
                        <th class="w-2/12 border border-gray-400 p-2 bg-gray-100">title</th>
                        <th class="w-2/12 border border-gray-400 p-2 bg-gray-100">h1</th>
                        <th class="w-3/12 border border-gray-400 p-2 bg-gray-100">description</th>
                        <th class="w-2/12 border border-gray-400 p-2 bg-gray-100">Дата проверки</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Пока пустая таблица, так как проверки еще не реализованы -->
                    <tr>
                        <td colspan="6" class="border border-gray-400 p-2 text-center text-gray-500">
                            Пока нет проверок
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </section>
`, page = page)