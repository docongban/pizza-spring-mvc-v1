
function Validator(formSelector){

    function getParent(element, selector) {
        while(element.parentElement){
            if(element.parentElement.matches(selector)){
                return element.parentElement
            }
            element = element.parentElement
        }
    }

    var formRules = {}

    // Quy ước tạo rule:
    // - Nếu có lỗi thì return 'error message'
    // - Nếu ko có lỗi thì return 'undefined'
    var validatorRules = {
        required: function(value){
            return value ? undefined : 'Vui lòng nhập trường này'
        },
        email: function(value){
            var regex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/
            return regex.test(value) ? undefined :'Trường này phải là email'
        },
        min: function(min){
            return function(value){
                return value.length >= min ? undefined : `Vui lòng nhập ít nhất ${min} kí tự`
            }
        },
        max: function(max){
            return function(value){
                return value.length <= max ? undefined : `Vui lòng nhập tối đa ${max} kí tự`
            }
        },
        password_confirm: function(value){
            var password = document.querySelector("#password").value
            if(value===password){
				return undefined;
			}else{
				return 'Nhập lại mật khẩu không đúng';
			}
        },
    }

    
    //Lấy ra formElement trong DOM theo formSelector 
    var formElement = document.querySelector(formSelector);

    // chỉ dùng khi có formElement trong DOM
    if(formElement){

        var inputs = formElement.querySelectorAll('[name][rules]')
        for(var input of inputs){

            var rules = input.getAttribute('rules').split('|')
            for(var rule of rules){
                var rulesInfor
                var isRuleHasValue = rule.includes(':')

                if(isRuleHasValue){
                    rulesInfor = rule.split(':')
                    rule = rulesInfor[0]
                }

                var ruleFunc = validatorRules[rule]

                if(isRuleHasValue){
                    ruleFunc = ruleFunc(rulesInfor[1])
                }

                if(Array.isArray(formRules[input.name])){
                    formRules[input.name].push(ruleFunc)
                }else{
                    formRules[input.name] = [ruleFunc]
                }
            }

            // Lắng nghe các sự kiện validate
            input.onblur = handleValidate
            input.oninput = handleClearError
        }

        // Hàm thực hiện validate
        function handleValidate(e) {
            var rules = formRules[e.target.name]
            var errorMessage 

            for(rule of rules) {
                errorMessage = rule(e.target.value)
            }

            // Nếu có lỗi thì hiển thị ra
            if(errorMessage){
                var formGroup = getParent(e.target, '.form-group')
                if(formGroup){
                    formGroup.classList.add('invalid')

                    var formMessage = formGroup.querySelector('.form-message')
                    if(formMessage){
                        formMessage.innerText = errorMessage
                    }
                }
            }
            return !errorMessage
        }

        // Hàm clear Message lỗi
        function handleClearError(e){
            var formGroup = getParent(e.target, '.form-group')
            if(formGroup.classList.contains('invalid')){
                formGroup.classList.remove('invalid')

                var formMessage = formGroup.querySelector('.form-message')
                if(formMessage){
                    formMessage.innerText = ''
                }
            }
        }
    }

    // Xử lí hành vi submit form 
    formElement.onsubmit = function(e){
        e.preventDefault()

        var inputs = formElement.querySelectorAll('[name][rules]')
        var isValid = true

        for(var input of inputs){
            if(!handleValidate({ target: input })){
                isValid = false
            }
        }

        if(isValid){
            formElement.submit()
        }
    }
}