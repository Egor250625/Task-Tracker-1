const DEBOUNCE_TIME = 500;
const API_URL = "http://localhost:8080/api";

$(function () {
    const tasks = {};

    function appendErrorToForm(error, element) {
        $(element).next('.form-group').remove();
        const errorDiv = $(`
            <div class="form-group mt-3">
                <div class="alert alert-danger mt-3"></div>
            </div>
        `);
        error.appendTo(errorDiv.find("div.alert"));
        errorDiv.insertAfter(element);
    }

    function removeErrorFromForm(error, element) {
        $(element).next().remove();
    }

    function updateTaskUI(authenticated) {
        if (authenticated) {
            $('#auth-hint').hide();
        } else {
            $('#auth-hint').show();
        }
    }

    function formatTime(time) {
        return new Date(time).toLocaleString('en-GB');
    }

    function createTaskDiv() {
        return $(`
            <a type="button" class="list-group-item list-group-item-action" 
               data-bs-toggle="modal" data-bs-target="#task-modal">
            </a>
        `);
    }

    function jsonTaskToTaskDiv(task) {
        const div = createTaskDiv();
        div.text(task.title).attr("data-task-id", task.id);
        return div;
    }

    function toggleError(element, active, text = '') {
        element.attr('hidden', !active);
        if (active) {
            element.find('div.alert').text(text);
        }
    }

    function updateTaskModal(task) {
        const isCompleted = task.status === "COMPLETED";

        $('#task-modal input[name="title"]').val(task.title);
        $('#task-modal textarea[name="description"]').val(task.description);
        $('#task-modal input[name="is-completed"]').prop("checked", isCompleted);
        $("#completed-at")
            .attr('hidden', !task.completedAt)
            .text(task.completedAt ? `Done at ${formatTime(task.completedAt)}` : '');
    }

    function updateNavbar(authenticated, userData = {}) {
        $("#login-button, #register-button").attr('hidden', authenticated);
        $("#user-email, #log-out-button").attr('hidden', !authenticated);

        if (authenticated && userData.email) {
            $("#email-text").text(userData.email);
        }
    }

    function authHeaders() {
        const token = localStorage.getItem("jwt");
        return token ? {Authorization: `Bearer ${token}`} : {};
    }

    // $(document).ready(function () {
        fetchTasks(); // 1. Показывает форму
        $('#create-task').on('submit', function (e) { // 2. Навешивает обработчик
            e.preventDefault();

            const form = $(this);
            const title = form.find('input[name="title"]').val();
            const description  = form.find('input[name="description"]').val();

            $.ajax({
                type: 'POST',
                url: `${API_URL}/task`,
                contentType: "application/json",
                headers: authHeaders(),
                data: JSON.stringify({
                    title: title,
                    description : description ,
                }),
                success: function (task) {
                    const taskDiv = jsonTaskToTaskDiv(task);
                    tasks[task.id] = task;

                    if (task.status === "COMPLETED") {
                        $("#done-tasks").append(taskDiv);
                    } else {
                        $("#to-do-tasks").append(taskDiv);
                    }

                    form.trigger('reset');
                },
                error: function (xhr) {
                    alert("Failed to create task, please try again.");
                }
            });
        });
    // });


    function fetchTasks() {
        $.ajax({
            type: 'GET',
            url: `${API_URL}/tasks`,
            headers: authHeaders(),
            success: function (data) {
                const doneTasks = $("#done-tasks").empty();
                const toDoTasks = $("#to-do-tasks").empty();

                data.forEach(task => {
                    const taskDiv = jsonTaskToTaskDiv(task);
                    tasks[task.id] = task;
                    if (task.status === "COMPLETED") {
                        doneTasks.append(taskDiv);
                    } else {
                        toDoTasks.append(taskDiv);
                    }
                });
                $('#tasks-container').attr('hidden', false);
            },
            error: function () {
                $('#tasks-container').attr('hidden', true);
            }
        });
    }

    $('#theme-switcher').on('click', function () {
        const htmlTag = $('html');
        const currentTheme = htmlTag.attr('data-bs-theme') || 'light';
        const newTheme = currentTheme === 'light' ? 'dark' : 'light';

        htmlTag.attr('data-bs-theme', newTheme);
        localStorage.setItem('theme', newTheme); // Сохраняем тему
    });

    $(function () {
        const savedTheme = localStorage.getItem('theme') || 'light';
        $('html').attr('data-bs-theme', savedTheme);
    });

    $('#task-modal').on('show.bs.modal', function (e) {
        const task = tasks[$(e.relatedTarget).data('task-id')];
        updateTaskModal(task);

        const modal = $(e.currentTarget);

        modal.find('button[name="delete"]').off('click').click(function () {
            $.ajax({
                type: 'DELETE',
                url: `${API_URL}/task`,
                contentType: "application/json",
                headers: authHeaders(),
                data: JSON.stringify({id: task.id}),
                success: function () {
                    $(`#to-do-tasks a[data-task-id="${task.id}"]`).remove();
                    $(`#done-tasks a[data-task-id="${task.id}"]`).remove();
                    modal.modal('hide');
                }
            });
        });

        // OK button — единая отправка
        modal.find('button.btn-success').off('click').click(function () {
            const title = modal.find('input[name="title"]').val().trim();
            const description = modal.find('textarea[name="description"]').val().trim();
            const isCompleted = modal.find('input[name="is-completed"]').prop('checked');

            if (title === "") {
                toggleError($('#task-modal-error'), true, "Title cannot be empty");
                return;
            }

            const status = isCompleted ? "COMPLETED" : "IN_PROGRESS";

            $.ajax({
                type: 'PATCH',
                url: `${API_URL}/task`,
                contentType: "application/json",
                headers: authHeaders(),
                data: JSON.stringify({
                    id: task.id,
                    title: title,
                    description: description,
                    status: status
                }),
                success: function (data) {
                    task.title = data.title;
                    task.description = data.description;
                    task.status = data.status;
                    task.completedAt = data.completedAt;

                    $(`a[data-task-id="${task.id}"]`).text(task.title);
                    fetchTasks();
                    toggleError($('#task-modal-error'), false);

                    modal.modal('hide');
                },
                error: function (xhr) {
                    let message = "Oops! Something went wrong...";
                    try {
                        if (xhr.responseText) {
                            const json = JSON.parse(xhr.responseText);
                            message = json.message || message;
                        }
                    } catch (_) {}
                    toggleError($('#task-modal-error'), true, message);
                }
            });
        });
    });

    function updateUserData() {
        $.ajax({
            type: 'GET',
            url: `${API_URL}/user`,
            headers: authHeaders(),
            success: function (data) {
                updateNavbar(true, data);
                fetchTasks();
            },
        });
    }

    const loginForm = $("#login-form");
    const registerForm = $("#register-form");

    function validateForm(form, rules, messages) {
        form.validate({
            rules,
            messages,
            errorPlacement: appendErrorToForm,
            success: removeErrorFromForm,
        });
    }

    function handleFormSubmit(form, route, modal, errorElement) {
        form.submit(function (e) {
            e.preventDefault();

            const formData = {};
            form.serializeArray().forEach(field => {
                formData[field.name] = field.value;
            });

            $.ajax({
                type: 'POST',
                url: API_URL + route,
                headers: {
                    ...authHeaders(),
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify(formData),
                success: function () {
                    modal.modal('hide');
                    toggleError(errorElement, false);
                    updateUserData();
                },
                error: function (xhr) {
                    toggleError(errorElement, true, xhr.responseText ? JSON.parse(xhr.responseText).message : "Oops! Something went wrong...");
                }
            });
        });
    }


    validateForm(loginForm, {
        email: {required: true, email: true},
        password: {required: true},
    }, {
        email: "Please enter a valid email address",
        password: "Please provide a password",
    });

    loginForm.submit(function (e) {
        e.preventDefault();

        const data = {
            email: $("#login-form input[name='email']").val(),
            password: $("#login-form input[name='password']").val()
        };

        $.ajax({
            type: 'POST',
            url: `${API_URL}/auth/login`,
            contentType: "application/json",
            data: JSON.stringify(data),
            success: function (data) {
                if (data.token) {
                    localStorage.setItem("jwt", data.token);
                    $('#login-modal').modal('hide');
                    toggleError($('#login-error'), false);
                    updateUserData();
                } else {
                    toggleError($('#login-error'), true, "No token received from server");
                }
            },
            error: function (xhr) {
                toggleError($('#login-error'), true, xhr.responseText ? JSON.parse(xhr.responseText).message : "Login failed.");
            }
        });
    });


    validateForm(registerForm, {
        email: {required: true, email: true},
        password: {required: true, minlength: 5},
        confirmPassword: {required: true, equalTo: "#registerPassword"},
    }, {
        email: 'Please enter a valid email address',
        password: {
            required: 'Please provide a password',
            minlength: 'Your password must be at least 5 characters long',
        },
        confirmPassword: {
            required: 'Please confirm your password',
            equalTo: 'Passwords do not match',
        },
    });

    registerForm.submit(function (e) {
        e.preventDefault();
        $.ajax({
            type: 'POST',
            url: `${API_URL}/auth/register`,
            contentType: "application/json",
            data: JSON.stringify({
                email: $("#register-form input[name='email']").val(),
                password: $("#register-form input[name='password']").val()
            }),
            success: function (data) {
                if (data.token) {
                    localStorage.setItem("jwt", data.token);
                    $('#register-modal').modal('hide');
                    toggleError($('#register-error'), false);
                    updateUserData();
                } else {
                    toggleError($('#register-error'), true, "No token received from server");
                }
            },
            error: function (xhr) {
                toggleError($('#register-error'), true, xhr.responseText ? JSON.parse(xhr.responseText).message : "Registration failed.");
            }
        });
    });

    $('#task-info-btn').click(function () {
        $.ajax({
            type: 'POST',
            url: `${API_URL}/task/info`,
            contentType: "application/json",
            headers: authHeaders(),
            success: function (data) {
                $('#task-info-result').html(`
                <p>Information was successfully sent to your email address.</p>
            `);
            },
            error: function () {
                $('#task-info-result').html(`
                <p>Failed to fetch user info.</p>
                <p>Could not send the email. Please try again later.</p>
            `);
            }
        });
    });

    $('#log-out-button').click(function () {
        localStorage.removeItem("jwt");
        updateNavbar(false);
        $('#tasks-container').attr('hidden', true);
    });

    updateUserData();
});

