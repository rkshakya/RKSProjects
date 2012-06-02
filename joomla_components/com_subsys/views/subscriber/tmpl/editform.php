<?php defined('_JEXEC') or die('Restricted access'); ?>

<form action="index.php" method="post" name="adminForm" id="adminForm">
<div class="col100">
	<fieldset class="adminform">
		<legend><?php echo JText::_( 'Subscriber Details' ); ?></legend>

		<table class="admintable">
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'ID' ); ?>:
				</label>
			</td>
			<td>
				<input class="text_area" type="text" name="sub_c" id="sub" size="32" maxlength="250" value="<?php echo $this->subscriber->sub_id;?>" />
			</td>
		</tr>
		<tr>
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Code' ); ?>:
				</label>
			</td>
			<td>
				<input class="text_area" type="text" name="sub_code" id="sub_code" size="32" maxlength="250" value="<?php echo $this->subscriber->sub_code;?>" />
			</td>
		</tr>
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Name' ); ?>:
				</label>
			</td>
			<td>
				<input class="text_area" type="text" name="sub_name" id="sub_name" size="32" maxlength="250" value="<?php echo $this->subscriber->sub_name;?>" />
			</td>
		</tr>
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Address' ); ?>:
				</label>
			</td>
			<td>
				<input class="text_area" type="text" name="sub_address" id="sub_address" size="32" maxlength="250" value="<?php echo $this->subscriber->sub_address;?>" />
			</td>
		</tr>
	</table>
	</fieldset>
</div>
<div class="clr"></div>

<input type="hidden" name="option" value="com_subsys" />
<input type="hidden" name="sub_id" value="<?php echo $this->subscriber->sub_id; ?>" />
<input type="hidden" name="task" value="" />
<input type="hidden" name="controller" value="subscriber" />
</form>
