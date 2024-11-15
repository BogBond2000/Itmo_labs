%ifndef C
%define C
%define point 0
%macro colon 2

    %ifnstr  %1
        %error "Key can not exists"
    %endif

    %ifnid %2
        %error "Label can not exists"
    %endif

    %2:
        dq point
        db %1, 0
        %define point %2

%endmacro

%endif
